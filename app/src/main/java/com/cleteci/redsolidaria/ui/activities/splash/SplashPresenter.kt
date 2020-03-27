package com.cleteci.redsolidaria.ui.activities.splash

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class SplashPresenter: SplashContract.Presenter {



    private val subscriptions = CompositeDisposable()
    private lateinit var view: SplashContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: SplashContract.View) {
        this.view = view
        view.init() // as default
    }




}