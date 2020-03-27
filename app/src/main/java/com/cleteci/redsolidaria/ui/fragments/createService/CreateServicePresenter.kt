package com.cleteci.redsolidaria.ui.fragments.createService

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class CreateServicePresenter: CreateServiceContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: CreateServiceContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CreateServiceContract.View) {
        this.view = view
        view.init() // as default
    }


}