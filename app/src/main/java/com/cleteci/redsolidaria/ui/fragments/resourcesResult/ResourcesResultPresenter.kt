package com.cleteci.redsolidaria.ui.fragments.resourcesResult

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationsByCategoryQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.models.Service
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResourcesResultPresenter: ResourcesResultContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResourcesResultContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResourcesResultContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(list: List<GetOrganizationsByCategoryQuery.ByIdOrOrgKeyWord>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()
        var sCategory = list
        if (sCategory != null) {
            for (organization in sCategory) {
                arrayList.add(
                    Service(
                        organization.id(),
                        organization.name().toString(),
                        LocalDataForUITest.getCategoryById("0")!!,
                        organization.hourHand().toString(),
                        organization.ranking().toString(),
                        "",
                        organization.location()?.name().toString(),
                        isGeneric =  false
                    )
                )//Adding object in arraylist
            }
        }

        return arrayList
    }

    override fun loadData(id: String, keyWord: String) {

        BaseApp.apolloClient.query(
            GetOrganizationsByCategoryQuery.builder().id(id).keyWord(keyWord)
                .build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationsByCategoryQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationsByCategoryQuery.Data>) {
                if (response.data() != null) {
                    var arrayList = deserializeResponse(response.data()?.byIdOrOrgKeyWord())
                    view.loadDataSuccess(arrayList)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }


}