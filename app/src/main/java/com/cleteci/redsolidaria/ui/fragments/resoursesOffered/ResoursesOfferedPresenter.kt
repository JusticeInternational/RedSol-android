package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationServicesQuery
import com.cleteci.redsolidaria.LoadUsedCategoriesQuery
import com.cleteci.redsolidaria.models.ResourceCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResoursesOfferedPresenter : ResoursesOfferedContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResoursesOfferedContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResoursesOfferedContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(list: List<GetOrganizationServicesQuery.User>?): ArrayList<ResourceCategory> {
        val arrayList = ArrayList<ResourceCategory>()
        var services = list!![0].ownerOf()?.services()!!
        for (service in services) {
            var serviceCategory = service.serviceCategory()
            val resources: Resources = BaseApp?.instance.resources
            val resourceId: Int = resources.getIdentifier(
                service.serviceCategory()?.icon(), "drawable",
                BaseApp?.instance.packageName)
            if (serviceCategory != null) {
                arrayList.add(
                    ResourceCategory(
                        serviceCategory.id(),
                        serviceCategory.name(),
                        serviceCategory.icon(),
                        resourceId
                    )
                )
            }//Adding object in arraylist
        }
        return arrayList
    }

    override fun getData() {
        BaseApp.apolloClient.query(
            GetOrganizationServicesQuery.builder().id(BaseApp.prefs.user_saved.toString()).build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationServicesQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationServicesQuery.Data>) {
                if (response.data() != null) {
                    var arrayList = deserializeResponse(response.data()?.User())
                    view.loadDataSuccess(arrayList)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }


}