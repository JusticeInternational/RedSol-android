package com.cleteci.redsolidaria.ui.fragments.users

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.models.User
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersListPresenter: AttendersListContract.Presenter {


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

        Log.d("TAG", "IMIN--"+serviceId+"---"+type+"---"+BaseApp.prefs.user_saved.toString())
if (type==1) {
    BaseApp.apolloClient.query(
        GetServedBeneficiariesServiceQuery.builder()
            .id(serviceId).orgID(BaseApp.prefs.current_org.toString())
            .build()
    ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesServiceQuery.Data>() {
        override fun onResponse(response: Response<GetServedBeneficiariesServiceQuery.Data>) {
            val users = ArrayList<User>()
            if (response.data() != null) {
                if (response.data()?.servedBeneficiariesService() != null)
                    for (user in response.data()?.servedBeneficiariesService()!!) {
                        users.add(
                            User(
                                "", user.name()!!, "","", user.email()
                            )
                        )

                    }
                view.showUsers(users)


                Log.d("TAG", "GOOD")
                // response.data()?.servedBeneficiariesService()

            }
        }

        override fun onFailure(e: ApolloException) {
            Log.d("TAG", "error")
        }
    })
}else{
    BaseApp.apolloClient.query(
        GetServedUnregisteredServiceQuery.builder()
            .id(serviceId).orgID(BaseApp.prefs.current_org.toString())
            .build()
    ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredServiceQuery.Data>() {
        override fun onResponse(response: Response<GetServedUnregisteredServiceQuery.Data>) {
            val users = ArrayList<User>()
            if (response.data() != null) {
                if (response.data()?.servedUnregisteredService()!=null)
                    for (user in response.data()?.servedUnregisteredService()!!) {
                        users.add(
                            User(
                                "", user.name()!!, user.lastName(),"", user.email()
                            )
                        )

                    }
                view.showUsers(users)


                Log.d("TAG", "GOOD")
                // response.data()?.servedBeneficiariesService()

            }
        }
        override fun onFailure(e: ApolloException) {
            Log.d("TAG", "error")
        }
    })
}
    }

    override fun loadDataCategory(categoryId: String, type: Int) {

        Log.d("TAG", "IMIN--"+categoryId+"---"+type+"---"+BaseApp.prefs.user_saved.toString())
if (type==1) {
    BaseApp.apolloClient.query(
        GetServedBeneficiariesCategoryQuery.builder()
            .id(categoryId)
            .orgID(BaseApp.prefs.current_org.toString())
            .build()
    ).enqueue(object : ApolloCall.Callback<GetServedBeneficiariesCategoryQuery.Data>() {
        override fun onResponse(response: Response<GetServedBeneficiariesCategoryQuery.Data>) {
            val users = ArrayList<User>()
            if (response.data() != null) {
                if (response.data()?.servedBeneficiariesCategory() != null)
                    for (user in response.data()?.servedBeneficiariesCategory()!!) {
                        users.add(
                            User(
                                "", user.name()!!, "","", user.email()
                            )
                        )

                    }
                view.showUsers(users)


                Log.d("TAG", "GOOD")
                // response.data()?.servedBeneficiariesService()

            }
        }

        override fun onFailure(e: ApolloException) {
            Log.d("TAG", "error")
        }
    })

}else{
    BaseApp.apolloClient.query(
        GetServedUnregisteredCategoryQuery.builder()
            .id(categoryId)
            .orgID(BaseApp.prefs.current_org.toString())
            .build()
    ).enqueue(object : ApolloCall.Callback<GetServedUnregisteredCategoryQuery.Data>() {
        override fun onResponse(response: Response<GetServedUnregisteredCategoryQuery.Data>) {
            val users = ArrayList<User>()
            if (response.data() != null) {
                if (response.data()?.servedUnregisteredCategory() != null)
                    for (user in response.data()?.servedUnregisteredCategory()!!) {
                        users.add(
                            User(
                                "", user.name()!!, user.lastName(),"", user.email()
                            )
                        )

                    }
                view.showUsers(users)


                Log.d("TAG", "GOOD")
                // response.data()?.servedBeneficiariesService()

            }
        }

        override fun onFailure(e: ApolloException) {
            Log.d("TAG", "error")
        }
    })
}
    }


}