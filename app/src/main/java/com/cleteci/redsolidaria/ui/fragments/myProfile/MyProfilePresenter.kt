package com.cleteci.redsolidaria.ui.fragments.myProfile

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfilePresenter: MyProfileContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyProfileContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyProfileContract.View) {
        this.view = view
        view.init() // as default
    }


}