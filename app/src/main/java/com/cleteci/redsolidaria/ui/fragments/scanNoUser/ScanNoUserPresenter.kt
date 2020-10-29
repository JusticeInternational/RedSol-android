package com.cleteci.redsolidaria.ui.fragments.infoService

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoadTotalServedCategoryQuery
import com.cleteci.redsolidaria.LoadTotalServedServiceQuery
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanNoUserPresenter: ScanNoUserContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ScanNoUserContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScanNoUserContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadCategoryData(id: String) {
        BaseApp.apolloClient.query(
            LoadTotalServedCategoryQuery.builder().id(id).orgID(BaseApp.prefs.current_org.toString()).build()
        ).enqueue(object : ApolloCall.Callback<LoadTotalServedCategoryQuery.Data>() {
            override fun onResponse(response: Response<LoadTotalServedCategoryQuery.Data>) {
                if (response.data() != null) {
                    view.loadDataSuccess(response.data()?.totalAtentionCategory()!!)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }

    override fun loadServiceData(id: String) {
        BaseApp.apolloClient.query(
            LoadTotalServedServiceQuery.builder().id(id).orgID(BaseApp.prefs.current_org.toString()).build()
        ).enqueue(object : ApolloCall.Callback<LoadTotalServedServiceQuery.Data>() {
            override fun onResponse(response: Response<LoadTotalServedServiceQuery.Data>) {
                if (response.data() != null) {
                    view.loadDataSuccess(response.data()?.totalAtentionService()!!)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }

}