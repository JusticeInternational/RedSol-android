package com.cleteci.redsolidaria.ui.fragments.changePassword

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.request.RequestHeaders
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.ChangePasswordMutation
import com.cleteci.redsolidaria.LoginUserMutation
import com.cleteci.redsolidaria.R
import io.reactivex.disposables.CompositeDisposable
import okhttp3.internal.http2.Header

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
        var t : RequestHeaders = RequestHeaders.builder().build()
        t.toBuilder().addHeader("Authorization",BaseApp.prefs.token.toString())
        if (newPass.isEmpty()){
            view.errorPass(BaseApp.instance.getString(R.string.wrong_pass))
        } else {
            BaseApp.apolloClient.mutate(
                ChangePasswordMutation.builder().password(newPass)
                    .build()
            ).requestHeaders(t).enqueue(object : ApolloCall.Callback<ChangePasswordMutation.Data>() {
                override fun onResponse(response: Response<ChangePasswordMutation.Data>) {
                    if (response.data() != null) {

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