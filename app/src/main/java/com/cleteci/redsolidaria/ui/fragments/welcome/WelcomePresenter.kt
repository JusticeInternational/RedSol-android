package com.cleteci.redsolidaria.ui.fragments.welcome

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class WelcomePresenter: WelcomeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: WelcomeContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: WelcomeContract.View) {
        this.view = view
        view.init() // as default
    }


}