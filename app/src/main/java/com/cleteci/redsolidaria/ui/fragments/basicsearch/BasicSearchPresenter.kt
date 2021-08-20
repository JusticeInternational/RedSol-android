package com.cleteci.redsolidaria.ui.fragments.basicsearch

import android.content.res.Resources
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetUsedCategoriesQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by ogulcan on 07/02/2018.
 */
class BasicSearchPresenter: BasicSearchContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: BasicSearchContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: BasicSearchContract.View) {
        this.view = view
        view.init() // as default
    }

    private fun deserializeResponse(list: List<GetUsedCategoriesQuery.UsedCategory>?): ArrayList<Category> {
        val arrayList = ArrayList<Category>()
        for (serviceCategory in list!!) {
            val resources: Resources = BaseApp.instance.resources
            var resourceId = resources.getIdentifier(
                serviceCategory.icon(), "drawable",
                BaseApp.instance.packageName)

            if (resourceId == 0) {
                resourceId = R.drawable.ic_general_category
            }

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

    override fun loadData() {
        BaseApp.apolloClient.query(
            GetUsedCategoriesQuery.builder().build()
        ).enqueue(object : ApolloCall.Callback<GetUsedCategoriesQuery.Data>() {
            override fun onResponse(response: Response<GetUsedCategoriesQuery.Data>) {
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
