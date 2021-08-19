package com.cleteci.redsolidaria.ui.fragments.changePassword

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.ChangePasswordMutation
import com.cleteci.redsolidaria.R
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ChangePassPresenter: ChangePassContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ChangePassContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ChangePassContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun verifyData(newPass: String) {
        if (newPass.isEmpty()){
            view.errorPass(BaseApp.instance.getString(R.string.wrong_pass))
        } else {
            BaseApp.apolloClient.mutate(
                ChangePasswordMutation.builder().password(newPass).token(BaseApp.sharedPreferences.token.toString())
                    .build()
            ).enqueue(object : ApolloCall.Callback<ChangePasswordMutation.Data>() {
                override fun onResponse(response: Response<ChangePasswordMutation.Data>) {
                    if (response.data() != null) {
                        BaseApp.sharedPreferences.token = response.data()?.changeUserPassword()
                        view.saved()
                    } else {
                        //view.errorEmailPass(BaseApp.instance.getString(R.string.wrong_login))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    //view.errorEmailPass(BaseApp.instance.getString(R.string.error_login))
                    Log.d("TAG", "error")
                }
            })
        }
    }
}
