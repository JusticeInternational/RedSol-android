package com.cleteci.redsolidaria.ui.fragments.users

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationServicesAndCategoriesQuery
import com.cleteci.redsolidaria.GetServedBeneficiariesServiceQuery
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersListPresenter: AttendersListContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: AttendersListContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: AttendersListContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadDataService(serviceId: String, type: Int) {

        Log.d("TAG", "IMIN--"+serviceId+"---"+type)

        BaseApp.apolloClient.query(
            GetServedBeneficiariesServiceQuery.builder()
                .id(serviceId).orgID(BaseApp.prefs.user_saved.toString())
                .build()
        ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesServiceQuery.Data>() {
            override fun onResponse(response: Response<GetServedBeneficiariesServiceQuery.Data>) {
                if (response.data() != null) {

                    Log.d("TAG", "GOOD")
                   // response.data()?.servedBeneficiariesService()

                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })

    }

    override fun loadDataCategory(categoryId: String, type: Int) {

    /*    BaseApp.apolloClient.query(
            GetOrganizationServicesAndCategoriesQuery
                .builder()
                .id(categoryId).idInput(BaseApp.prefs.user_saved.toString())
                .build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationServicesAndCategoriesQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationServicesAndCategoriesQuery.Data>) {
                if (response.data() != null) {

                    Log.d("TAG", "GOOD")
                    // response.data()?.servedBeneficiariesService()

                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
*/

    }


}