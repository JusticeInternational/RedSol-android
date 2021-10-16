package com.cleteci.redsolidaria.ui.activities.register

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.RegisterUserMutation
import com.cleteci.redsolidaria.ResetPasswordMutation
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class RegisterPresenter : RegisterContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: RegisterContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RegisterContract.View) {
        this.view = view
        view.init() // as default
    }


    override fun validateRegister(term: Boolean, policies: Boolean, name: String, lastName: String, email: String, password: String) {
        if (!term) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.accept_term_condition))
        } else if (!policies) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.accept_privacy_policies))
        } else if (name.isEmpty() || name.length < 1) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_name))
        } else if (lastName.isEmpty() || lastName.length < 1) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_last_name))
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.replace(" ", "")).matches()) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_email))
        } else if (password.isEmpty() || password.length < 4) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_pass))
        } else {
            createUser(name, lastName, email, password)
        }
    }

    fun createUser(name: String, lastName: String, email: String, password: String) {
        BaseApp.apolloClient.mutate(
            RegisterUserMutation.builder().name(name).lastName(lastName).email(email).password(password)
                .build()
        ).enqueue(object: ApolloCall.Callback<RegisterUserMutation.Data>() {
            override fun onResponse(response: Response<RegisterUserMutation.Data>) {
                if(response.data()?.CreateUser() != null) {
                    view.askCode()
                } else {
                    view.emailExists()
                }
            }
            override fun onFailure(e: ApolloException) {
                view.showError(BaseApp.instance.getString(R.string.error_server))
                Log.d("TAG", "error")
            }
        })
    }

    override fun receiveUser() {
        view.goToLogin()
    }

    override fun goToRegister() {
        view.tryAgain()
    }
}