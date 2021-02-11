package com.cleteci.redsolidaria.ui.fragments.users

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersPresenter: AttendersContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: AttendersContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: AttendersContract.View) {
        this.view = view
        view.init() // as default
    }


}