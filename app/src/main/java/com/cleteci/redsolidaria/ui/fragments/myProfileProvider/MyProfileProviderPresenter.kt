package com.cleteci.redsolidaria.ui.fragments.myProfileProvider

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfileProviderPresenter: MyProfileProviderContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyProfileProviderContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyProfileProviderContract.View) {
        this.view = view
        view.init() // as default
    }


}