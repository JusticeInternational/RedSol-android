package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
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

   private fun deserializeResponse(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        var categories = list!![0].ownerOf()?.serviceCategories()!!
        for (serviceCategory in categories) {

            val resources: Resources = BaseApp?.instance.resources
            val resourceId: Int = resources.getIdentifier(
                serviceCategory?.icon(), "drawable",
                BaseApp?.instance.packageName)

                arrayList.add(
                    Category(
                        serviceCategory.id(),
                        serviceCategory.name(),
                        resourceId,
                        0,
                        serviceCategory.description(),
                        serviceCategory.icon()
                    )
                )

        }
        return arrayList
    }

    private fun deserializeResponseGeneric(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {

        val arrayList = ArrayList<Service>()
        var services = list!![0].ownerOf()?.services()!!
        for (service in services) {
            if (service.isGeneral==true){
        val resources: Resources = BaseApp?.instance.resources
        val resourceId: Int = resources.getIdentifier(
            "ic_check_green", "drawable",
            BaseApp?.instance.packageName
        )

        arrayList.add(
            Service(
                service.id(),
                service.name()!!,
                LocalDataForUITest.getCategoryById("0")!!,
                "",
                "0",
                "",
                "",
                service.description(),
                service.isGeneral!!
            )

        )}

    }

        return arrayList
    }

    private fun deserializeResponseServices(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()

        var services = list!![0].ownerOf()?.services()!!
        for (service in services) {
             var serviceCategory = service.serviceCategory()
            if (serviceCategory != null && service.isGeneral==false) {
            val resources: Resources = BaseApp?.instance.resources
            var resourceId: Int= resources.getIdentifier(
                serviceCategory?.icon(), "drawable",
                BaseApp?.instance.packageName)
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