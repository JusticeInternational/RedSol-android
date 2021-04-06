package com.cleteci.redsolidaria.ui.fragments.map

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getCategoriesList
import com.cleteci.redsolidaria.models.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MapPresenter: MapContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MapContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MapContract.View) {
        this.view = view
        view.init()
    }

    override fun loadData() {
        view.loadDataSuccess(getCategoriesList())
    }


}