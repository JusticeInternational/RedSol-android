package com.cleteci.redsolidaria.ui.fragments.contactUs

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ContactUsPresenter: ContactUsContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ContactUsContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ContactUsContract.View) {
        this.view = view
        view.init() // as default
    }


}