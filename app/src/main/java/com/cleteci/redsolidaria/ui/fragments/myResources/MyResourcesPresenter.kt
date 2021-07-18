package com.cleteci.redsolidaria.ui.fragments.myResources

import com.cleteci.redsolidaria.data.LocalDataForUITest
import io.reactivex.disposables.CompositeDisposable

class MyResourcesPresenter: MyResourcesContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyResourcesContract.View

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyResourcesContract.View) {
        this.view = view
        view.init()
    }

    override fun getData() {
        view.loadDataSuccess(
                LocalDataForUITest.getServicesList().subList(0, 2),
                LocalDataForUITest.getServicesList().subList(2, 7),
                LocalDataForUITest.getServicesList().subList(3, 5),
                LocalDataForUITest.getServicesList().subList(5, 6)
        )
    }

}