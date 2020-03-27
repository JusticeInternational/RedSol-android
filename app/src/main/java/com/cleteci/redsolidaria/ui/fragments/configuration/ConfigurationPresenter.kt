package com.cleteci.redsolidaria.ui.fragments.configuration

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ConfigurationPresenter: ConfigurationContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ConfigurationContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ConfigurationContract.View) {
        this.view = view
        view.init() // as default
    }


}