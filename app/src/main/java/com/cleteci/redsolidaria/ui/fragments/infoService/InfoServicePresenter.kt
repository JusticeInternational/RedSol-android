package com.cleteci.redsolidaria.ui.fragments.infoService

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class InfoServicePresenter: InfoServiceContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: InfoServiceContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: InfoServiceContract.View) {
        this.view = view
        view.init() // as default
    }


}