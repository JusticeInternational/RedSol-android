package com.cleteci.redsolidaria.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.util.getCategoryIconByIconString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeneralViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    val usedCategories = MutableLiveData<List<Category>>()
    val categories = MutableLiveData<List<GetServiceCategoriesQuery.ServiceCategory>>()
    val services = MutableLiveData<List<Service>>()

    fun getUsedCategories() {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getUsedCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetUsedCategoriesQuery.Data> ->
                    usedCategories.value = if (response.data == null ||
                        response.data?.usedCategories().isNullOrEmpty()) {
                        ArrayList()
                    } else {
                        deserializeResponse(response.data!!.usedCategories())
                    }.distinct()
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )

    }

    fun getServiceCategories() {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getServiceCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetServiceCategoriesQuery.Data> ->
                    categories.value = if (response.data == null ||
                        response.data?.ServiceCategories().isNullOrEmpty()) {
                        ArrayList()
                    } else {
                        ArrayList(response.data!!.ServiceCategories().orEmpty())
                    }.distinct()
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun getServices() {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getServices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetServicesQuery.Data> ->
                    services.value = if (response.data == null ||
                        response.data?.Services().isNullOrEmpty()) {
                        ArrayList()
                    } else {
                        ArrayList(deserializeServices(response.data!!.Services().orEmpty()))
                    }.distinct()
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                })
        )
    }

    private fun deserializeResponse(categories: List<GetUsedCategoriesQuery.UsedCategory>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        for (category in categories!!) {
            arrayList.add(
                Category(
                    category.id(),
                    category.name(),
                    getCategoryIconByIconString(category.icon()),
                    icon = category.icon()
                )
            )
        }
        return arrayList
    }

    private fun deserializeServices(services: List<GetServicesQuery.Service>): ArrayList<Service> {
        val arrayList = ArrayList<Service>()

        for (service in services) {
            val serviceCategory = service.serviceCategory()
            if (serviceCategory != null) {
                val category = Category(
                    serviceCategory.id(),
                    serviceCategory.name(),
                    getCategoryIconByIconString(serviceCategory.icon()),
                    0,
                    serviceCategory.description(),
                    serviceCategory.icon()
                )
                arrayList.add(
                    Service(
                        service.id(),
                        service.name()!!,
                        category,
                        "",
                        serviceCategory.id(),
                        "",
                        "",
                        service.description(),
                        service.isGeneral ?: false
                    )
                )
            }
        }
        return arrayList
    }

    companion object {
        const val TAG: String = "GeneralViewModel"

    }

}