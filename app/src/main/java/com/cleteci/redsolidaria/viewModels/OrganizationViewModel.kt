package com.cleteci.redsolidaria.viewModels


import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationById
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationsList
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.ui.search.OrganizationsCategorySearchAdapter.OrganizationCategory
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.getTestDataPreference
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.putOrganizationAttributes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OrganizationViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    val organizationLists = MutableLiveData<Organization.OrganizationLists>()
    val organizationsCategoryList = MutableLiveData<ArrayList<OrganizationCategory>>()

    fun getOrganization(id: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getOrganization(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetOrganizationInfoQuery.Data> ->
                    if (response.data == null || response.data?.User().isNullOrEmpty()
                        || response.data?.User()?.get(0)?.ownerOf() == null
                    ) {
                        status.value = QueryStatus.ORGANIZATION_NOT_FOUND

                        if (getTestDataPreference()) {
                            val organization =
                                LocalDataForUITest.getOrganizationsList()[0]// Using test data
                            BaseApp.sharedPreferences.currentOrganizationId = organization.id
                            putOrganizationAttributes(organization)
                        }

                    } else {
                        BaseApp.sharedPreferences.currentOrganizationId =
                            response.data?.User()?.get(0)?.ownerOf()?.id()
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun getServicesAndCategories(id: String) {
        status.value = QueryStatus.NOTIFY_LOADING

        if (getTestDataPreference()) {
            val organization = getOrganizationById("0")!!
            val services = organization.servicesList!!
            val categories: ArrayList<Category> = ArrayList()
            for (service in services) {
                categories.add(service.category)
            }
            organizationLists.value = Organization.OrganizationLists(services, categories, services)
            status.value = QueryStatus.NOTIFY_SUCCESS
        } else {
            compositeDisposable.add(
                graphQLController.getOrganizationServicesAndCategories(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<GetOrganizationServicesAndCategoriesQuery.Data> ->
                        if (response.data == null || response.data?.User().isNullOrEmpty()
                            || response.data?.User()?.get(0)?.ownerOf() == null
                        ) {
                            organizationLists.value = Organization.OrganizationLists(
                                ArrayList(),
                                ArrayList(),
                                ArrayList()
                            )
                        } else {
                            val user = response.data?.User()
                            val arrayListServices =
                                deserializeResponseServices(response.data?.User())
                            val arrayList = deserializeResponse(response.data?.User())
                            val arrayListGenericServices =
                                deserializeResponseGeneric(response.data?.User())
                            organizationLists.value = Organization.OrganizationLists(
                                arrayListServices,
                                arrayList,
                                arrayListGenericServices
                            )
                        }
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }, {
                        status.value = QueryStatus.NOTIFY_FAILURE
                        Log.d(TAG, it.message)
                    })
            )
        }
    }

    fun getOrganizationsByCategory(categoryId: String, keyWord: String, categoryIconId: Int, categoryName: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getOrganizationsByCategory(categoryId, keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetOrganizationsByCategoryQuery.Data> ->
                    organizationsCategoryList.value =
                        if (response.data == null || response.data?.byIdOrOrgKeyWord().isNullOrEmpty())
                            ArrayList()
                        else
                            deserializeOrganizationsByCategory(response.data?.byIdOrOrgKeyWord(),
                                categoryId,
                                categoryIconId,
                                categoryName).distinctBy { it.organizationName }  as ArrayList<OrganizationCategory>
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    private fun deserializeOrganizationsByCategory(
        list: List<GetOrganizationsByCategoryQuery.ByIdOrOrgKeyWord>?,
        categoryId: String,
        categoryIconId: Int,
        categoryName: String): ArrayList<OrganizationCategory> {
        val arrayList = ArrayList<OrganizationCategory>()
        if (list != null) {
            for (organization in list) {
                val service = Service(
                    organization.id(),
                    organization.name() ?: "",
                    Category(id = categoryId, iconId = categoryIconId),
                    organization.hourHand().toString(),
                    organization.ranking().toString(),
                    "",
                    organization.location()?.name().toString(),
                    isGeneric = false
                )

                arrayList.add(
                    OrganizationCategory(
                        "0",//organization.id(),
                        R.drawable.organization_logo_sample,
                        organization.name() ?: "",
                        organization.ranking() ?: 0,
                        organization.hourHand().toString(),
                        "40Km",// TODO calculate
                        categoryName,
                        categoryIconId
                    )
                )
            }
        }

        return arrayList
    }


    private fun deserializeResponse(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        val resources: Resources = BaseApp.instance.resources
        val categories = if (!list.isNullOrEmpty()) list[0].ownerOf()?.serviceCategories()
            .orEmpty() else emptyList()

        for (serviceCategory in categories) {
            val resourceId: Int = resources.getIdentifier(
                serviceCategory?.icon(), "drawable",
                BaseApp.instance.packageName
            )
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
        val resources: Resources = BaseApp.instance.resources

        val services =
            if (!list.isNullOrEmpty()) list[0].ownerOf()?.services().orEmpty() else emptyList()
        for (service in services) {
            if (service.isGeneral == true) {
                val resourceId: Int = resources.getIdentifier(
                    "ic_check_green",
                    "drawable",
                    BaseApp.instance.packageName
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
                )
            }
        }

        return arrayList
    }

    private fun deserializeResponseServices(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()
        val resources: Resources = BaseApp.instance.resources

        val services =
            if (!list.isNullOrEmpty()) list[0].ownerOf()?.services().orEmpty() else emptyList()
        for (service in services) {
            val serviceCategory = service.serviceCategory()
            if (serviceCategory != null && service.isGeneral == false) {

                var resourceId: Int = resources.getIdentifier(
                    serviceCategory.icon(), "drawable",
                    BaseApp.instance.packageName
                )

                arrayList.add(
                    Service(
                        service.id(),
                        service.name()!!,
                        LocalDataForUITest.getCategoryById("0")!!,
                        "",
                        serviceCategory.id(),
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

    companion object {
        const val TAG: String = "OrganizationViewModel"

    }

}