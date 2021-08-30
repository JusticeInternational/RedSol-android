package com.cleteci.redsolidaria.ui.fragments.users

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.data.LocalDataForUITest.ROLE_BENEFICIARY
import com.cleteci.redsolidaria.models.User
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersListPresenter : AttendersListContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: AttendersListContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: AttendersListContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadDataService(serviceId: String, type: Int) {
        Log.d(
            "TAG",
            "IMIN--" + serviceId + "---" + type + "---" + BaseApp.sharedPreferences.userSaved.toString()
        )
        if (type == 1) {
            BaseApp.apolloClient.query(
                GetServedBeneficiariesServiceQuery.builder()
                    .id(serviceId).orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesServiceQuery.Data>() {
                override fun onResponse(response: Response<GetServedBeneficiariesServiceQuery.Data>) {
                    val users = ArrayList<User>()
                    if (response.data() != null) {
                        val usersList = response.data()?.servedBeneficiariesService()
                        if (!usersList.isNullOrEmpty()) {
                            for (i in 0 until usersList.size step 4) {
                                val user = usersList[i]
                                users.add(
                                    User("", user.name()!!, "", "", user.email())
                                )
                            }
                        }
                        view.showUsers(users)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "error")
                }
            })
        } else {
            BaseApp.apolloClient.query(
                GetServedUnregisteredServiceQuery.builder()
                    .id(serviceId).orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredServiceQuery.Data>() {
                override fun onResponse(response: Response<GetServedUnregisteredServiceQuery.Data>) {
                    val users = ArrayList<User>()
                    if (response.data() != null) {
                        val usersList = response.data()?.servedUnregisteredService()
                        if (!usersList.isNullOrEmpty()) {
                            for (i in 0 until usersList.size step 4) {
                                val user = usersList[i]
                                users.add(
                                    User("", user.name()!!, "", "", user.email())
                                )
                            }
                        }
                        view.showUsers(users)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "error")
                }
            })
        }
    }

    override fun loadDataCategory(categoryId: String, type: Int) {
        Log.d(
            "TAG",
            "IMIN--" + categoryId + "---" + type + "---" + BaseApp.sharedPreferences.userSaved.toString()
        )
        if (type == 1) {
            BaseApp.apolloClient.query(
                GetServedBeneficiariesCategoryQuery.builder()
                    .id(categoryId)
                    .orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesCategoryQuery.Data>() {
                override fun onResponse(response: Response<GetServedBeneficiariesCategoryQuery.Data>) {
                    val users = ArrayList<User>()
                    if (response.data() != null) {
                        val usersList = response.data()?.servedBeneficiariesCategory()
                        if (!usersList.isNullOrEmpty()) {
                            for (i in 0 until usersList.size step 4) {
                                val user = usersList[i]
                                users.add(
                                    User("", user.name()!!, "", "", user.email())
                                )
                            }
                        }
                        view.showUsers(users)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "error")
                }
            })

        } else {
            BaseApp.apolloClient.query(
                GetServedUnregisteredCategoryQuery.builder()
                    .id(categoryId)
                    .orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                    .build()
            ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredCategoryQuery.Data>() {
                override fun onResponse(response: Response<GetServedUnregisteredCategoryQuery.Data>) {
                    val users = ArrayList<User>()
                    if (response.data() != null) {
                        val usersList = response.data()?.servedUnregisteredCategory()
                        if (!usersList.isNullOrEmpty()) {
                            for (i in 0 until usersList.size step 4) {
                                val user = usersList[i]
                                users.add(
                                    User("", user.name()!!, "", "", user.email())
                                )
                            }
                        }
                        view.showUsers(users)
                    }
                }

                override fun onFailure(e: ApolloException) {
                    Log.d("TAG", "error")
                }
            })
        }
    }


}