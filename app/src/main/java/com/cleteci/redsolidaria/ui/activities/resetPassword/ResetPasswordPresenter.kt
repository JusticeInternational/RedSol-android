package com.cleteci.redsolidaria.ui.activities.resetPassword

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ResetPasswordMutation
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResetPasswordPresenter : ResetPasswordContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResetPasswordContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResetPasswordContract.View) {
        this.view = view
        view.init() // as default
    }


    override fun validateRegister(email: String) {

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.replace(" ", "")).matches()) {
            view.showError(BaseApp.instance.getString(R.string.wrong_email))
        } else {
            BaseApp.apolloClient.mutate(
                ResetPasswordMutation.builder().email(email)
                .build()
            ).enqueue(object: ApolloCall.Callback<ResetPasswordMutation.Data>() {
                override fun onResponse(response: Response<ResetPasswordMutation.Data>) {
                    if(response.data() != null) {
                        view.askCode()
                    }
                }
                override fun onFailure(e: ApolloException) {
                    view.showError(BaseApp.instance.getString(R.string.error_server))
                    Log.d("TAG", "error")
                }
            })
        }
    }
}
