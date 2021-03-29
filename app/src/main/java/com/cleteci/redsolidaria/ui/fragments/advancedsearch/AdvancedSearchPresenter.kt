package com.cleteci.redsolidaria.ui.fragments.advancedsearch

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.LoadUsedCategoriesQuery
import com.cleteci.redsolidaria.models.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AdvancedSearchPresenter: AdvancedSearchContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: AdvancedSearchContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: AdvancedSearchContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(list: List<LoadUsedCategoriesQuery.UsedCategory>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        for (serviceCategory in list!!) {
            val resources: Resources = BaseApp.instance.resources
            val resourceId: Int = resources.getIdentifier(
                serviceCategory.icon(), "drawable",
                BaseApp.instance.packageName)
            arrayList.add(
                Category(
                    serviceCategory.id(),
                    serviceCategory.name(),
                    resourceId,
                    icon = serviceCategory.icon()
                )
            )//Adding object in arraylist
        }
        return arrayList
    }

    override fun search(data: String) {
        BaseApp.apolloClient.query(
            LoadUsedCategoriesQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<LoadUsedCategoriesQuery.Data>() {
            override fun onResponse(response: Response<LoadUsedCategoriesQuery.Data>) {
                if (response.data() != null) {
                    var arrayList = deserializeResponse(response.data()?.usedCategories())
                    view.loadDataSuccess(arrayList)
                }
            }
            override fun onFailure(e: ApolloException) {
                Log.d("TAG", "error")
            }
        })
    }
}
