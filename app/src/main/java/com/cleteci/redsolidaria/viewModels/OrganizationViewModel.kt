package com.cleteci.redsolidaria.viewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.data.LocalDataForUITest.getGeneralCategory
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationByIdTest
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationsList
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.ui.search.OrganizationsCategorySearchAdapter.OrganizationCategory
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.getTestDataPreference
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.putOrganizationAttributes
import com.cleteci.redsolidaria.util.getIcon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OrganizationViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    val organizationLists = MutableLiveData<Organization.OrganizationLists>()
    val organizationsCategoryList = MutableLiveData<ArrayList<OrganizationCategory>>()
    val totalCategoryAttentions = MutableLiveData<Int>()
    val totalServiceAttentions = MutableLiveData<Int>()

    fun getOrganizationByUserId(userId: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getOrganizationByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetOrganizationByUserIdQuery.Data> ->
                    if (response.data == null || response.data?.User().isNullOrEmpty()
                        || response.data?.User()?.get(0)?.ownerOf() == null
                        || response.data?.User()?.get(0)?.ownerOf()?.fragments()?.organizationDetails() == null
                    ) {
                        status.value = QueryStatus.ORGANIZATION_NOT_FOUND

                        if (getTestDataPreference()) {
                            val organization =
                                getOrganizationsList()[0]// Using test data
                            BaseApp.sharedPreferences.currentOrganizationId = organization.id
                            putOrganizationAttributes(organization)
                        }

                    } else {
                        val user = response.data!!.User()!![0]
                        val organization = user?.ownerOf()?.fragments()?.organizationDetails()
                        BaseApp.sharedPreferences.currentOrganizationId = organization!!.id()
                        putOrganizationAttributes(user.email(), organization)
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun getOrganizationById(organizationId: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getOrganizationById(organizationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetOrganizationByIdQuery.Data> ->
                    if (response.data == null || response.data?.Organization().isNullOrEmpty()
                        || response.data?.Organization()?.get(0) == null
                        || response.data?.Organization()?.get(0)?.fragments()?.organizationDetails() == null
                    ) {
                        status.value = QueryStatus.ORGANIZATION_NOT_FOUND

                        if (getTestDataPreference()) {
                            val organization =
                                getOrganizationsList()[0]// Using test data
                            BaseApp.sharedPreferences.currentOrganizationId = organization.id
                            putOrganizationAttributes(organization)
                        }

                    } else {
                        val organization = response.data!!.Organization()!![0].fragments().organizationDetails()
                        BaseApp.sharedPreferences.currentOrganizationId = organization.id()
                        putOrganizationAttributes("", organization)
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
            val organization = getOrganizationByIdTest("0")!!
            val services = organization.servicesList!!
            val categories: ArrayList<Category> = ArrayList()
            for (service in services) {
                val ca =   Category(service.serviceCategory!!.id(),service.serviceCategory!!.name(),
                    getIcon(service.serviceCategory!!.icon()),0,service.serviceCategory!!.description())
                categories.add(ca)
            }
            organizationLists.value = Organization.OrganizationLists(services, categories)
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
                                ArrayList()
                            )
                        } else {
                            organizationLists.value = Organization.OrganizationLists(
                                deserializeOfferedServices(response.data?.User()),
                                deserializeOfferedCategories(response.data?.User())
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

    fun getOrganizationsByCategory(
        categoryId: String,
        keyWord: String,
        categoryIconId: Int,
        categoryName: String
    ) {
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
                            deserializeOrganizationsByCategory(
                                response.data?.byIdOrOrgKeyWord(),
                                categoryId,
                                categoryIconId,
                                categoryName
                            ).distinctBy { it.organizationName } as ArrayList<OrganizationCategory>
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun getTotalCategoryAttentions(categoryId: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getTotalCategoryAttentions(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetTotalCategoryAttentionsQuery.Data> ->
                    val total = response.data?.totalAtentionCategory() ?: 0
                    totalCategoryAttentions.value = if (total != 0 && total >= 4) total / 4 else total
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    fun getTotalServiceAttentions(categoryId: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getTotalServiceAttentions(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetTotalServiceAttentionsQuery.Data> ->
                    val total = response.data?.totalAtentionService() ?: 0
                    totalServiceAttentions.value = if (total != 0 && total >= 4) total / 4 else total
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
        categoryName: String
    ): ArrayList<OrganizationCategory> {
        val arrayList = ArrayList<OrganizationCategory>()
        if (list != null) {
            for (organization in list) {
                arrayList.add(
                    OrganizationCategory(
                        organization.id(),
                        R.drawable.organization_logo_sample,
                        organization.name() ?: "",
                        organization.ranking() ?: 0,
                        organization.hourHand().toString(),
                        categoryName,
                        categoryIconId
                    )
                )
            }
        }

        return arrayList
    }

    private fun deserializeOfferedCategories(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        val categories = if (!list.isNullOrEmpty()) list[0].ownerOf()?.serviceCategories()
            .orEmpty() else emptyList()

        for (serviceCategory in categories) {
            arrayList.add(getCategory(serviceCategory))
        }
        return arrayList
    }


    private fun deserializeOfferedServices(list: List<GetOrganizationServicesAndCategoriesQuery.User>?): ArrayList<Service> {
        val arrayList = ArrayList<Service>()

        val services =
            if (!list.isNullOrEmpty()) list[0].ownerOf()?.services().orEmpty() else emptyList()
        for (service in services) {
            val serviceCategory = service.serviceCategory()
            if (serviceCategory != null) {
                arrayList.add(
                    Service(
                        service.id(),
                        service.name()!!,
                        getCategory(serviceCategory),
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

    private fun getCategory(anyObject: Any): Category {
        return when (anyObject) {
            is GetOrganizationServicesAndCategoriesQuery.ServiceCategory -> {
                Category(
                    anyObject.id(),
                    anyObject.name(),
                    getCategoryIconByIconString(anyObject.icon()),
                    0,
                    anyObject.description(),
                    anyObject.icon()
                )
            }
            is GetOrganizationServicesAndCategoriesQuery.ServiceCategory1 -> {
                Category(
                    anyObject.id(),
                    anyObject.name(),
                    getCategoryIconByIconString(anyObject.icon()),
                    0,
                    anyObject.description(),
                    anyObject.icon()
                )
            }
            else -> getGeneralCategory()
        }
    }

    fun createUnregisteredUserAttention(request: CreateAttentionRequest) {
        status.value = QueryStatus.NOTIFY_LOADING
        val disposable = if (request.categoryId.isNotEmpty()) {
            graphQLController.createAttentionCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ProvideAtentionUnregisteredCategoryMutation.Data> ->
                    if (response.data != null) {
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    } else {
                        status.value = QueryStatus.NOTIFY_FAILURE
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        } else {
            graphQLController.createAttentionService(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ProvideAtentionUnregisteredServiceMutation.Data> ->
                    if (response.data != null) {
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    } else {
                        status.value = QueryStatus.NOTIFY_FAILURE
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        }
        compositeDisposable.add(disposable)

    }

    fun createOrganization(request: CreateOrganizationRequest) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.createOrganization(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CreateOrganizationMutation.Data> ->
                    if (response.data != null) {
                        associateOrganizationToUser(request.id)
                    } else {
                        status.value = QueryStatus.NOTIFY_FAILURE
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )
    }

    private fun associateOrganizationToUser(organizationId: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        val userId = BaseApp.sharedPreferences.userSaved
        if (!userId.isNullOrEmpty()) {
            compositeDisposable.add(
                graphQLController.associateOrganizationToUser(userId, organizationId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<AssociateOrganizationToUserMutation.Data> ->
                        if (response.data != null) {
                            updateUserRoleToAdmin()
                        } else {
                            status.value = QueryStatus.NOTIFY_FAILURE
                        }
                    }, {
                        status.value = QueryStatus.NOTIFY_FAILURE
                        Log.d(TAG, it.message)
                    })
            )
        } else {
            status.value = QueryStatus.NOTIFY_FAILURE
        }
    }

    private fun updateUserRoleToAdmin() {
        status.value = QueryStatus.NOTIFY_LOADING
        val userId = BaseApp.sharedPreferences.userSaved
        if (!userId.isNullOrEmpty()) {
            compositeDisposable.add(
                graphQLController.updateUserRoleToAdmin(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: Response<UpdateUserRoleMutation.Data> ->
                        if (response.data != null) {
                            status.value = QueryStatus.NOTIFY_SUCCESS
                        } else {
                            status.value = QueryStatus.NOTIFY_FAILURE
                        }
                    }, {
                        status.value = QueryStatus.NOTIFY_FAILURE
                        Log.d(TAG, it.message)
                    })
            )
        } else {
            status.value = QueryStatus.NOTIFY_FAILURE
        }
    }

    class CreateAttentionRequest(
        val serviceId: String = "",
        val categoryId: String = "",
        val name: String,
        val lastName: String,
        val identification: String = "",
        val gender: Int = 0,
        val country: String = "",
        val age: Int = 0,
        val phone: String = "",
        val email: String = "",
        val otherInfo: String = "",
        val nameService: String = "",
        val isGeneric: Boolean
    )

    class CreateOrganizationRequest(
        val id: String = "",
        val name: String = "",
        val phone: String = "",
        val webPage: String = "",
        val ranking: String = "",
        val hourHand: String = "",
        val description: String = "",
        val servicesDesc: String = "",
        val iconName: String = "",
        val urlIcon: String = ""

    )


    companion object {
        const val TAG: String = "OrganizationViewModel"

        fun getCategoryIconByIconString(name: String): Int {
            return when (name) {
                "food" -> R.drawable.ic_food
                "cross" -> R.drawable.ic_cross
                "transport" -> R.drawable.ic_transport
                "education" -> R.drawable.ic_education
                else -> R.drawable.ic_general_category
            }
        }
    }

}