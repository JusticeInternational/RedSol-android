package com.cleteci.redsolidaria.viewModels

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetCategoriesQuery
import com.cleteci.redsolidaria.GetUsedCategoriesQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getCategoriesList
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.network.GraphQLController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GeneralViewModel(private val graphQLController: GraphQLController) : BaseViewModel() {

    val usedCategories = MutableLiveData<ArrayList<Category>>()
    val categories = MutableLiveData<ArrayList<Category>>()

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
                    }.distinct() as ArrayList<Category>
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )

    }

    fun getCategories() {
        status.value = QueryStatus.NOTIFY_LOADING
        compositeDisposable.add(
            graphQLController.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<GetCategoriesQuery.Data> ->
                    categories.value = if (response.data == null ||
                        response.data?.Categories().isNullOrEmpty()) {
                        ArrayList()
                    } else {
                        deserializeResponse(response.data!!.Categories())
                    }.distinct() as ArrayList<Category>
                    status.value = QueryStatus.NOTIFY_SUCCESS
                }, {
                    status.value = QueryStatus.NOTIFY_FAILURE
                    Log.d(TAG, it.message)
                })
        )

    }

    private fun deserializeResponse(categories: List<Any>?): java.util.ArrayList<Category> {
        val resources: Resources = BaseApp.instance.resources
        val arrayList = ArrayList<Category>()
            for (category in categories!!) {
                if (category is GetCategoriesQuery.Category ) {
                    var resourceId = resources.getIdentifier(
                        category.icon(), "drawable",
                        BaseApp.instance.packageName)

                    if (resourceId == 0) {
                        resourceId =  R.drawable.ic_general_category
                        for (localCategory in getCategoriesList()) {
                            if (category.name().equals(localCategory.name, ignoreCase = true)) {
                                resourceId = localCategory.iconId
                                break
                            }
                        }
                    }

                    arrayList.add(
                        Category(
                            category.id(),
                            category.name(),
                            resourceId,
                            icon = category.icon()
                        )
                    )
                } else if (category  is GetUsedCategoriesQuery.UsedCategory) {
                    var resourceId = resources.getIdentifier(
                        category.icon(), "drawable",
                        BaseApp.instance.packageName)

                    if (resourceId == 0) {
                        resourceId =  R.drawable.ic_general_category
                        for (localCategory in getCategoriesList()) {
                            if (category.name().equals(localCategory.name, ignoreCase = true)) {
                                resourceId = localCategory.iconId
                                break
                            }
                        }
                    }

                    arrayList.add(
                        Category(
                            category.id(),
                            category.name(),
                            resourceId,
                            icon = category.icon()
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