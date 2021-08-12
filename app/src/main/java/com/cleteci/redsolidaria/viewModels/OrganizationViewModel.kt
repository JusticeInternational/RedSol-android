package com.cleteci.redsolidaria.viewModels

import android.util.Log
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationInfoQuery
import com.cleteci.redsolidaria.network.GraphQLController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class OrganizationViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    fun getOrganization(id: String) {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getOrganization(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetOrganizationInfoQuery.Data> ->
                    if (response.data == null ||  response.data?.User().isNullOrEmpty()
                        || response.data?.User()?.get(0)?.ownerOf() == null) {
                        status.value = QueryStatus.ORGANIZATION_NOT_FOUND
                        BaseApp.prefs.current_org = "0" // Using test data
                    } else {
                        BaseApp.prefs.current_org = response.data?.User()?.get(0)?.ownerOf()?.id()
                        status.value = QueryStatus.NOTIFY_SUCCESS
                    }
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                }))
    }

    companion object {
        const val TAG: String = "OrganizationViewModel"

    }

}