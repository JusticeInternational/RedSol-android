package com.cleteci.redsolidaria.viewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.data.LocalDataForUITest
import com.cleteci.redsolidaria.models.Beneficiary
import com.cleteci.redsolidaria.network.GraphQLController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class BeneficiaryViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    val resourcesLists = MutableLiveData<Beneficiary.ResourcesLists>()

    fun getResources() {
        resourcesLists.value = Beneficiary.ResourcesLists(
            LocalDataForUITest.getServicesList().subList(0, 2),
            LocalDataForUITest.getServicesList().subList(2, 7),
            LocalDataForUITest.getServicesList().subList(3, 5),
            LocalDataForUITest.getServicesList().subList(5, 6))
        status.value = QueryStatus.NOTIFY_SUCCESS

//        status.value = QueryStatus.NOTIFY_LOADING
//        compositeDisposable.add(
//            graphQLController.getUsedCategories()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response: Response<GetUsedCategoriesQuery.Data> ->
//
//                    status.value = QueryStatus.NOTIFY_SUCCESS
//
//                }, {
//                    status.value = QueryStatus.NOTIFY_FAILURE
//                    Log.d(TAG, it.message)
//                })
//        )

    }

    companion object {
        const val TAG: String = "BeneficiaryViewModel"

    }
}