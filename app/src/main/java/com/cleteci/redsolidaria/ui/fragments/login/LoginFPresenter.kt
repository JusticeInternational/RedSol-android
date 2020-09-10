package com.cleteci.redsolidaria.ui.fragments.login

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.R
import io.reactivex.disposables.CompositeDisposable

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

    override fun validateEmailPass(email: String, pass: String) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.replace(" ","")).matches()) {

            view.errorEmailPass(BaseApp.instance?.getString(R.string.wrong_email))
        } else if (pass.length<1){
            view.errorEmailPass(BaseApp.instance?.getString(R.string.wrong_pass))
        } else {
            val apolloClient = ApolloClient.builder().serverUrl("http://138.68.1.17:4000/graphql").build()
            apolloClient.mutate(LoginUserMutation.builder().email(email).password(pass)
                .build()
            ).enqueue(object: ApolloCall.Callback<LoginUserMutation.Data>() {
                override fun onResponse(response: Response<LoginUserMutation.Data>) {
                    if(response.data() != null) {
                        view.validEmailPass()
                    }else{

                        view.errorEmailPass(BaseApp.instance?.getString(R.string.wrong_login))
                    }
                }
                override fun onFailure(e: ApolloException) {
                    view.errorEmailPass(BaseApp.instance?.getString(R.string.error_login))
                    Log.d("TAG", "error")
                }
            })
        }

    }
}
