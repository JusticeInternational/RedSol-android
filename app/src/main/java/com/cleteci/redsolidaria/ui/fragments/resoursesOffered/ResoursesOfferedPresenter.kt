package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationServicesAndCategoriesQuery
import com.cleteci.redsolidaria.GetOrganizationServicesQuery
import com.cleteci.redsolidaria.LoadUsedCategoriesQuery
import com.cleteci.redsolidaria.models.Resource
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

   private fun deserializeResponse(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<ResourceCategory> {
        val arrayList = ArrayList<ResourceCategory>()
        var categories = list!![0].ownerOf()?.serviceCategories()!!
        for (serviceCategory in categories) {

            val resources: Resources = BaseApp?.instance.resources
            val resourceId: Int = resources.getIdentifier(
                serviceCategory?.icon(), "drawable",
                BaseApp?.instance.packageName)

                arrayList.add(
                    ResourceCategory(
                        serviceCategory.id(),
                        serviceCategory.name(),
                        serviceCategory.icon(),
                        resourceId, serviceCategory.description()
                    )
                )

        }
        return arrayList
    }

    private fun deserializeResponseGeneric(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Resource> {

        //Cableado mientras se crea servicio genérico
        val arrayList = ArrayList<Resource>()

        if (list!![0].ownerOf()?.services()!!.size>0) {

            var service = list!![0].ownerOf()?.services()!![0]

            var serviceCategory = service.serviceCategory()
            val resources: Resources = BaseApp?.instance.resources
            val resourceId: Int = resources.getIdentifier(
                "ic_check_green", "drawable",
                BaseApp?.instance.packageName
            )
            if (serviceCategory != null) {
                arrayList.add(
                    Resource(
                        service.id(), "Servicio genérico",
                        "", "", serviceCategory.id(),
                        "", resourceId, ""
                    )

                )
            }//Adding object in arraylist
        }

        return arrayList
    }

    private fun deserializeResponseServices(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Resource> {
        val arrayList = ArrayList<Resource>()

        var services = list!![0].ownerOf()?.services()!!
        for (service in services) {
             var serviceCategory = service.serviceCategory()
            val resources: Resources = BaseApp?.instance.resources
            val resourceId: Int = resources.getIdentifier(
                serviceCategory?.icon(), "drawable",
                BaseApp?.instance.packageName)
            if (serviceCategory != null) {
                arrayList.add(
                    Resource(service.id(),service.name()!!,
                "","",serviceCategory.id(),
                "", resourceId, "")

                )
            }
        }
        return arrayList
    }

    override fun getData() {
        BaseApp.apolloClient.query(
            GetOrganizationServicesAndCategoriesQuery.builder().id(BaseApp.prefs.user_saved.toString()).build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationServicesAndCategoriesQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationServicesAndCategoriesQuery.Data>) {
                if (response.data() != null) {
                    var arrayListServices = deserializeResponseServices(response.data()?.User())
                    var arrayList = deserializeResponse(response.data()?.User())
                    var arrayListGenericSerivices = deserializeResponseGeneric(response.data()?.User())
                    view.loadDataSuccess(arrayList, arrayListServices, arrayListGenericSerivices)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }


}