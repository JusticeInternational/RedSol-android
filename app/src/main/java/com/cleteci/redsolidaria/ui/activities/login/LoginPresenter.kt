package com.cleteci.redsolidaria.ui.activities.login

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class LoginPresenter: LoginContract.Presenter {



    private val subscriptions = CompositeDisposable()
    private lateinit var view: LoginContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoginContract.View) {
        this.view = view
        view.init() // as default
    }





}