package com.cleteci.redsolidaria.ui.fragments.resourcesOffered

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationById
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import io.reactivex.disposables.CompositeDisposable

class ResourcesOfferedPresenter : ResourcesOfferedContract.Presenter {

    private lateinit var view: ResourcesOfferedContract.View
    private val subscriptions = CompositeDisposable()

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResourcesOfferedContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        val resources: Resources = BaseApp.instance.resources
        val categories = if (!list.isNullOrEmpty()) list[0].ownerOf()?.serviceCategories().orEmpty() else emptyList()

        for (serviceCategory in categories) {
            val resourceId: Int = resources.getIdentifier(
                    serviceCategory?.icon(), "drawable",
                    BaseApp.instance.packageName)
            arrayList.add(Category(
                    serviceCategory.id(),
                    serviceCategory.name(),
                    resourceId,
                    0,
                    serviceCategory.description(),
                    serviceCategory.icon()
            ))

        }
        return arrayList
    }

    private fun deserializeResponseGeneric(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()
        val resources: Resources = BaseApp.instance.resources

        val services = if (!list.isNullOrEmpty()) list[0].ownerOf()?.services().orEmpty() else emptyList()
        for (service in services) {
            if (service.isGeneral == true) {
                val resourceId: Int = resources.getIdentifier(
                        "ic_check_green",
                        "drawable",
                        BaseApp.instance.packageName
                )
                arrayList.add(
                        Service(service.id(),
                                service.name()!!,
                                LocalDataForUITest.getCategoryById("0")!!,
                                "",
                                "0",
                                "",
                                "",
                                service.description(),
                                service.isGeneral!!
                        )
                )
            }
        }

        return arrayList
    }

    private fun deserializeResponseServices(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()
        val resources: Resources = BaseApp.instance.resources

        val services = if (!list.isNullOrEmpty()) list[0].ownerOf()?.services().orEmpty() else emptyList()
        for (service in services) {
            val serviceCategory = service.serviceCategory()
            if (serviceCategory != null && service.isGeneral == false) {

                var resourceId: Int = resources.getIdentifier(
                        serviceCategory.icon(), "drawable",
                        BaseApp.instance.packageName)

                arrayList.add(
                        Service(service.id(),
                                service.name()!!,
                                LocalDataForUITest.getCategoryById("0")!!,
                                "",
                                serviceCategory.id(),
                                "",
                                "",
                                service.description(),
                                service.isGeneral!!)

                )
            }
        }
        return arrayList
    }

    override fun getData() {
//        val organization = getOrganizationById("0")!!
//        val services = organization.servicesList!!
//        val categories = java.util.ArrayList<Category>()
//        for (service in services) {
//            categories.add(service.category)
//        }
//        view.loadDataSuccess(categories, services, services)

        BaseApp.apolloClient.query(
            GetOrganizationServicesAndCategoriesQuery.builder().id(BaseApp.prefs.user_saved.toString()).build()
        ).enqueue(object : ApolloCall.Callback<GetOrganizationServicesAndCategoriesQuery.Data>() {
            override fun onResponse(response: Response<GetOrganizationServicesAndCategoriesQuery.Data>) {
                if (response.data != null) {
                    val arrayListServices = deserializeResponseServices(response.data?.User())
                    val arrayList = deserializeResponse(response.data?.User())
                    val arrayListGenericServices = deserializeResponseGeneric(response.data?.User())
                    view.loadDataSuccess(arrayList, arrayListServices, arrayListGenericServices)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }


}