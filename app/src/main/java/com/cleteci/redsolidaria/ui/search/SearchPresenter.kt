package com.cleteci.redsolidaria.ui.search

import com.cleteci.redsolidaria.data.LocalDataForUITest.getCategoriesList
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class SearchPresenter: SearchContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SearchContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: SearchContract.View) {
        this.view = view
        view.init()
    }

    override fun loadData() {
        view.loadDataSuccess(getCategoriesList())
    }


}