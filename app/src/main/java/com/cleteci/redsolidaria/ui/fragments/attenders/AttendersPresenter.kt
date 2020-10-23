package com.cleteci.redsolidaria.ui.fragments.users

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersPresenter: UsersContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: UsersContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: UsersContract.View) {
        this.view = view
        view.init() // as default
    }


}