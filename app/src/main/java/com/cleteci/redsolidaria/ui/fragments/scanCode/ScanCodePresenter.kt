package com.cleteci.redsolidaria.ui.fragments.scanCode

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.*
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanCodePresenter : ScanCodeContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ScanCodeContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScanCodeContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun countService(userid: String, serviceid: String, nameService:String , isGeneric:Boolean) {

        BaseApp.apolloClient.mutate(
            ProvideAtentionServiceMutation.builder()
                .orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                .userID(userid)
                .serviceID(serviceid)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionServiceMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionServiceMutation.Data>) {

                if (response.data() != null && response.data()?.provideAtentionService() != null ) {
                    var name= response.data()?.provideAtentionService()!!.recipient()!!.name()

                    if (isGeneric){
                        view.showSuccessMsg( String.format(BaseApp.instance.getResources().getString(R.string.posted_service_2),name ))
                    }else{
                        view.showSuccessMsg( String.format(BaseApp.instance.getResources().getString(R.string.posted_service),name, nameService ))
                    }

                } else {
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                }
            }

            override fun onFailure(e: ApolloException) {

                view.showErrorMsg(BaseApp.instance.getString(R.string.error_posted_service))
            }
        })

    }

    override fun countCategory(userid: String, categoriyid: String, namecat:String) {

        BaseApp.apolloClient.mutate(
            ProvideAtentionCategoryMutation.builder()
                .orgID(BaseApp.sharedPreferences.currentOrganizationId.toString())
                .categoryID(categoriyid)
                .userID(userid)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionCategoryMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionCategoryMutation.Data>) {

                if (response.data() != null && response.data()?.provideAtentionCategory() != null ) {
                    var name= response.data()?.provideAtentionCategory()!!.recipient()!!.name()

                    view.showSuccessMsg( String.format(BaseApp.instance.getResources().getString(R.string.posted_category),name,namecat ))

                } else {
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                }
            }

            override fun onFailure(e: ApolloException) {

                view.showErrorMsg(BaseApp.instance.getString(R.string.error_posted_category))
            }
        })


    }


}