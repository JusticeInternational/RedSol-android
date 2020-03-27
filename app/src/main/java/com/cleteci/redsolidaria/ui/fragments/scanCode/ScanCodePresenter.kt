package com.cleteci.redsolidaria.ui.fragments.scanCode

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanCodePresenter: ScanCodeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ScanCodeContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ScanCodeContract.View) {
        this.view = view
        view.init() // as default
    }


}