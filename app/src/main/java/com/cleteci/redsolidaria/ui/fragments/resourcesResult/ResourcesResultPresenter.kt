package com.cleteci.redsolidaria.ui.fragments.resourcesResult

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationsByCategoryQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resource
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

    private fun deserializeResponse(list: List<GetOrganizationsByCategoryQuery.ByIdOrOrgKeyWord>?): ArrayList<Resource> {
        val arrayList = ArrayList<Resource>()
        var sCategory = list
        if (sCategory != null) {
            for (organization in sCategory!!) {
                arrayList.add(
                    Resource(
                        id = organization.id(),
                        name = organization.name().toString(),
                        hourHand = organization.hourHand().toString(),
                        ranking = organization.ranking().toString(),
                        photo = R.drawable.ic_sun2,
                        cate = "Testing",
                        location = organization.location()?.name().toString(),description =""
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