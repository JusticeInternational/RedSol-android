package com.cleteci.redsolidaria.ui.fragments.changePassword

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ChangePassPresenter: ChangePassContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ChangePassContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ChangePassContract.View) {
        this.view = view
        view.init() // as default
    }


}