package com.cleteci.redsolidaria.ui.fragments.login

import android.util.Base64
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationInfoQuery
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.data.LocalDataForUITest.ROLE_ORGANIZATION
import com.cleteci.redsolidaria.models.User
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import java.nio.charset.Charset

/**
 * Created by ogulcan on 07/02/2018.
 */
class LoginFPresenter : LoginFContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: LoginFContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoginFContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun getUser(response: String): User {
        val token: List<String> = response.split(".")
        val payload = token[1]
        val decodedBytes: ByteArray = Base64.decode(payload, Base64.URL_SAFE)
        val decodedData = String(decodedBytes, Charset.forName("UTF-8"))
        var formatter = Gson()
        return formatter.fromJson(decodedData, User::class.java)
    }

    override fun validateEmailPass(emailStr: String, pass: String) {
        val email= emailStr.trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError(BaseApp.instance.getString(R.string.wrong_email))
        } else if (pass.isEmpty()) {
            view.showError(BaseApp.instance.getString(R.string.wrong_pass))
        } else {
            loginUserMutation(email, pass)
            //uiTestLogin(email, pass)
        }
    }

    private fun uiTestLogin(email: String, pass: String) {
        val user = LocalDataForUITest.uiTestLogin(email, pass)
        if (user != null) {
            BaseApp.sharedPreferences.userSaved = user.id
            if (user.role == ROLE_ORGANIZATION) {
                BaseApp.sharedPreferences.isProviderService = true
                BaseApp.sharedPreferences.currentOrganizationId = LocalDataForUITest.getOrganizationByUserId(user.id)?.id
            }
            view.validEmailPass()
        } else {
            view.showError(BaseApp.instance.getString(R.string.wrong_login))
        }
    }

    private fun loginUserMutation(email: String, pass: String) {
        BaseApp.apolloClient.mutate(
            LoginUserMutation.builder().email(email).password(pass)
                .build()
        ).enqueue(object : ApolloCall.Callback<LoginUserMutation.Data>() {
            override fun onResponse(response: Response<LoginUserMutation.Data>) {
                if (response.data() != null) {
                    var token = response.data()?.login().toString()
                    val user = getUser(token)
                    BaseApp.sharedPreferences.isProviderService = user.role == "admin"
                    BaseApp.sharedPreferences.userSaved = user.id
                    BaseApp.sharedPreferences.token = token

                    if (user.role == "admin") {
                        getInfoOrganization()
                    }else {
                        view.validEmailPass()
                    }
                } else {
                    view.showError(BaseApp.instance.getString(R.string.wrong_login))
                }
            }

            override fun onFailure(e: ApolloException) {
                view.showError(BaseApp.instance.getString(R.string.error_login))
                Log.d("TAG", "error")
            }
        })
    }

    fun getInfoOrganization () {
        BaseApp.apolloClient.query(
            GetOrganizationInfoQuery.builder().id(BaseApp.sharedPreferences.userSaved.toString()).build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationInfoQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationInfoQuery.Data>) {
                if (response.data() != null) {
                    BaseApp.sharedPreferences.currentOrganizationId = response.data()?.User()?.get(0)?.ownerOf()?.id()
                    view.validEmailPass()
                }
            }
            override fun onFailure(e: ApolloException) {
                view.showError(BaseApp.instance.getString(R.string.error_login))
            }
        })

    }
}
