package com.cleteci.redsolidaria.ui.fragments.myProfileProvider

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationInfoQuery
import com.cleteci.redsolidaria.UpdateOrganizationMutation
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.models.Organization
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfileProviderPresenter: MyProfileProviderContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyProfileProviderContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyProfileProviderContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(org: GetOrganizationInfoQuery.User?): Organization? {
        var organization = org?.ownerOf()
        return if (organization != null) {
            Organization(
                organization.id(),
                LocalDataForUITest.getUserById(organization.id())!!,
                organization.name().toString(),
                0.0, 0.0,
                "",
                organization.location()?.name().toString(),
                organization.location()?.name().toString(),
                organization.webPage().toString(),
                organization.phone().toString(),
                organization.plan()?.name().toString(),
                null,
                null,
                organization.description().toString(),
                organization.description().toString(),
                organization.description().toString()
            )
        } else {
            return null
        }
    }

    override fun loadData() {
        BaseApp.apolloClient.query(
            GetOrganizationInfoQuery.builder().id(BaseApp.sharedPreferences.userSaved.toString()).build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationInfoQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationInfoQuery.Data>) {
                if (response.data() != null) {
                    val org = deserializeResponse(response.data()?.User()?.get(0))
                    if(org != null ) {
                        BaseApp.sharedPreferences.currentOrganizationId = org.id
                        view.loadDataSuccess(org)
                    }
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }

    override fun updateOrg(name: String, webPage: String, phone: String, aboutUs: String, servDesc: String) {
        BaseApp.apolloClient.mutate(
            UpdateOrganizationMutation.builder().id(BaseApp.sharedPreferences.currentOrganizationId.toString()).name(name).webPage(webPage)
                .phone(phone).aboutUs(aboutUs).servDesc(servDesc)
                .build()
        ).enqueue(object : ApolloCall.Callback<UpdateOrganizationMutation.Data>() {
            override fun onResponse(response: Response<UpdateOrganizationMutation.Data>) {
                if (response.data() != null) {
                    view.savedSuccess()
                }
            }

            override fun onFailure(e: ApolloException) {
                //view.errorEmailPass(BaseApp.instance.getString(R.string.error_login))
                Log.d("TAG", "error")
            }
        })
    }
}