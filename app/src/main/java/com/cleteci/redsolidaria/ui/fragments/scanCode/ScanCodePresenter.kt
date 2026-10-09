package com.cleteci.redsolidaria.ui.fragments.scanCode

import android.util.Log
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

    override fun countService(userid: String, serviceid: String, nameService: String, isGeneric: Boolean) {
        val cleanUserId = userid.trim()
        if (cleanUserId.isBlank()) {
            Log.e(TAG, "Scanned user ID is blank")
            view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
            return
        }

        val orgId = BaseApp.sharedPreferences.currentOrganizationId
        if (orgId.isNullOrBlank()) {
            Log.e(TAG, "No organization ID configured in SharedPreferences")
            view.showErrorMsg(BaseApp.instance.getString(R.string.error_getting_information))
            return
        }

        BaseApp.apolloClient.mutate(
            ProvideAtentionServiceMutation.builder()
                .orgID(orgId)
                .userID(cleanUserId)
                .serviceID(serviceid)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionServiceMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionServiceMutation.Data>) {
                if (response.hasErrors()) {
                    val errors = response.errors?.joinToString("; ") { it.message.orEmpty() }
                    Log.e(TAG, "GraphQL mutation errors: $errors")
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                    return
                }

                val attention = response.data?.provideAtentionService()
                val recipientName = attention?.recipient()?.name()

                if (attention != null && !recipientName.isNullOrBlank()) {
                    if (isGeneric) {
                        view.showSuccessMsg(
                            String.format(BaseApp.instance.resources.getString(R.string.posted_service_2), recipientName)
                        )
                    } else {
                        view.showSuccessMsg(
                            String.format(BaseApp.instance.resources.getString(R.string.posted_service), recipientName, nameService)
                        )
                    }
                } else {
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                }
            }

            override fun onFailure(e: ApolloException) {
                Log.e(TAG, "GraphQL network/query failure: ${e.message}", e)
                view.showErrorMsg(BaseApp.instance.getString(R.string.error_posted_service))
            }
        })
    }

    override fun countCategory(userid: String, categoriyid: String, namecat: String) {
        val cleanUserId = userid.trim()
        if (cleanUserId.isBlank()) {
            Log.e(TAG, "Scanned user ID is blank")
            view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
            return
        }

        val orgId = BaseApp.sharedPreferences.currentOrganizationId
        if (orgId.isNullOrBlank()) {
            Log.e(TAG, "No organization ID configured in SharedPreferences")
            view.showErrorMsg(BaseApp.instance.getString(R.string.error_getting_information))
            return
        }

        BaseApp.apolloClient.mutate(
            ProvideAtentionCategoryMutation.builder()
                .orgID(orgId)
                .categoryID(categoriyid)
                .userID(cleanUserId)
                .build()
        ).enqueue(object : ApolloCall.Callback<ProvideAtentionCategoryMutation.Data>() {
            override fun onResponse(response: Response<ProvideAtentionCategoryMutation.Data>) {
                if (response.hasErrors()) {
                    val errors = response.errors?.joinToString("; ") { it.message.orEmpty() }
                    Log.e(TAG, "GraphQL mutation errors: $errors")
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                    return
                }

                val attention = response.data?.provideAtentionCategory()
                val recipientName = attention?.recipient()?.name()

                if (attention != null && !recipientName.isNullOrBlank()) {
                    view.showSuccessMsg(
                        String.format(BaseApp.instance.resources.getString(R.string.posted_category), recipientName, namecat)
                    )
                } else {
                    view.showErrorMsg(BaseApp.instance.getString(R.string.invalid_user))
                }
            }

            override fun onFailure(e: ApolloException) {
                Log.e(TAG, "GraphQL network/query failure: ${e.message}", e)
                view.showErrorMsg(BaseApp.instance.getString(R.string.error_posted_category))
            }
        })
    }

    companion object {
        private const val TAG = "ScanCodePresenter"
    }
}
