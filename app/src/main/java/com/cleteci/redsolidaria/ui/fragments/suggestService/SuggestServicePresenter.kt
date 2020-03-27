package com.cleteci.redsolidaria.ui.fragments.suggestService

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class SuggestServicePresenter: SuggestServiceContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SuggestServiceContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: SuggestServiceContract.View) {
        this.view = view
        view.init() // as default
    }


}