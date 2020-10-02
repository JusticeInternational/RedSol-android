package com.cleteci.redsolidaria.ui.fragments.login

import android.util.Base64
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.R
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

    override fun validateEmailPass(email: String, pass: String) {
        var email=email.replace(" ", "") //delete empty spaces
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.errorEmailPass(BaseApp.instance.getString(R.string.wrong_email))
        } else if (pass.isEmpty()){
            view.errorEmailPass(BaseApp.instance.getString(R.string.wrong_pass))
        } else {
            BaseApp.apolloClient.mutate(
                LoginUserMutation.builder().email(email).password(pass)
                    .build()
            ).enqueue(object : ApolloCall.Callback<LoginUserMutation.Data>() {
                override fun onResponse(response: Response<LoginUserMutation.Data>) {
                    if (response.data() != null) {
                        var token = response.data()?.login().toString()
                        val user = getUser(token)
                        BaseApp.prefs.is_provider_service = user.role == "admin"
                        BaseApp.prefs.user_saved = user.id
                        BaseApp.prefs.token = token
                        view.validEmailPass()
                    } else {
                        view.errorEmailPass(BaseApp.instance.getString(R.string.wrong_login))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    view.errorEmailPass(BaseApp.instance.getString(R.string.error_login))
                    Log.d("TAG", "error")
                }
            })
        }
    }
}
