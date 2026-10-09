package com.cleteci.redsolidaria.ui.fragments.users

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.models.User
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersListPresenter : AttendersListContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: AttendersListContract.View

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: AttendersListContract.View) {
        this.view = view
        view.init()
    }

    override fun loadDataService(serviceId: String, type: Int) {
        val orgId = BaseApp.sharedPreferences.currentOrganizationId
        if (orgId.isNullOrBlank()) {
            Log.e(TAG, "Cannot load attenders: currentOrganizationId is null or empty")
            view.showUsers(emptyList())
            return
        }

        if (type == 1) {
            BaseApp.apolloClient.query(
                GetServedBeneficiariesServiceQuery.builder()
                    .id(serviceId)
                    .orgID(orgId)
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesServiceQuery.Data>() {
                override fun onResponse(response: Response<GetServedBeneficiariesServiceQuery.Data>) {
                    if (response.hasErrors()) {
                        Log.e(TAG, "GraphQL error in loadDataService (type 1): ${response.errors?.joinToString("; ") { it.message }}")
                    }
                    val users = ArrayList<User>()
                    val usersList = response.data?.servedBeneficiariesService()
                    if (!usersList.isNullOrEmpty()) {
                        for (user in usersList) {
                            val id = user.id().orEmpty()
                            val name = user.name().orEmpty()
                            val lastName = user.lastName().orEmpty()
                            val email = user.email().orEmpty()
                            users.add(User(id = id, name = name, lastName = lastName, email = email, role = ""))
                        }
                    }
                    view.showUsers(users)
                }

                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, "Apollo failure in loadDataService (type 1): ${e.message}", e)
                    view.showUsers(emptyList())
                }
            })
        } else {
            BaseApp.apolloClient.query(
                GetServedUnregisteredServiceQuery.builder()
                    .id(serviceId)
                    .orgID(orgId)
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredServiceQuery.Data>() {
                override fun onResponse(response: Response<GetServedUnregisteredServiceQuery.Data>) {
                    if (response.hasErrors()) {
                        Log.e(TAG, "GraphQL error in loadDataService (type 2): ${response.errors?.joinToString("; ") { it.message }}")
                    }
                    val users = ArrayList<User>()
                    val usersList = response.data?.servedUnregisteredService()
                    if (!usersList.isNullOrEmpty()) {
                        for (user in usersList) {
                            val id = user.id().orEmpty()
                            val name = user.name().orEmpty()
                            val lastName = user.lastName().orEmpty()
                            val email = user.email().orEmpty()
                            users.add(User(id = id, name = name, lastName = lastName, email = email, role = ""))
                        }
                    }
                    view.showUsers(users)
                }

                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, "Apollo failure in loadDataService (type 2): ${e.message}", e)
                    view.showUsers(emptyList())
                }
            })
        }
    }

    override fun loadDataCategory(categoryId: String, type: Int) {
        val orgId = BaseApp.sharedPreferences.currentOrganizationId
        if (orgId.isNullOrBlank()) {
            Log.e(TAG, "Cannot load category attenders: currentOrganizationId is null or empty")
            view.showUsers(emptyList())
            return
        }

        if (type == 1) {
            BaseApp.apolloClient.query(
                GetServedBeneficiariesCategoryQuery.builder()
                    .id(categoryId)
                    .orgID(orgId)
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesCategoryQuery.Data>() {
                override fun onResponse(response: Response<GetServedBeneficiariesCategoryQuery.Data>) {
                    if (response.hasErrors()) {
                        Log.e(TAG, "GraphQL error in loadDataCategory (type 1): ${response.errors?.joinToString("; ") { it.message }}")
                    }
                    val users = ArrayList<User>()
                    val usersList = response.data?.servedBeneficiariesCategory()
                    if (!usersList.isNullOrEmpty()) {
                        for (user in usersList) {
                            val name = user.name().orEmpty()
                            val email = user.email().orEmpty()
                            users.add(User(id = "", name = name, lastName = "", email = email, role = ""))
                        }
                    }
                    view.showUsers(users)
                }

                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, "Apollo failure in loadDataCategory (type 1): ${e.message}", e)
                    view.showUsers(emptyList())
                }
            })
        } else {
            BaseApp.apolloClient.query(
                GetServedUnregisteredCategoryQuery.builder()
                    .id(categoryId)
                    .orgID(orgId)
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredCategoryQuery.Data>() {
                override fun onResponse(response: Response<GetServedUnregisteredCategoryQuery.Data>) {
                    if (response.hasErrors()) {
                        Log.e(TAG, "GraphQL error in loadDataCategory (type 2): ${response.errors?.joinToString("; ") { it.message }}")
                    }
                    val users = ArrayList<User>()
                    val usersList = response.data?.servedUnregisteredCategory()
                    if (!usersList.isNullOrEmpty()) {
                        for (user in usersList) {
                            val name = user.name().orEmpty()
                            val lastName = user.lastName().orEmpty()
                            val email = user.email().orEmpty()
                            users.add(User(id = "", name = name, lastName = lastName, email = email, role = ""))
                        }
                    }
                    view.showUsers(users)
                }

                override fun onFailure(e: ApolloException) {
                    Log.e(TAG, "Apollo failure in loadDataCategory (type 2): ${e.message}", e)
                    view.showUsers(emptyList())
                }
            })
        }
    }

    companion object {
        private const val TAG = "AttendersListPresenter"
    }
}
