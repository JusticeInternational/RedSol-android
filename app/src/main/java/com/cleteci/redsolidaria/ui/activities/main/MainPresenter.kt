package com.cleteci.redsolidaria.ui.activities.main

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MainPresenter: MainContract.Presenter {



    private val subscriptions = CompositeDisposable()
    private lateinit var view: MainContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun onNavSearchOption() {
        view.showSearchFragment()
    }

    override fun onNavResourcesOption() {
        view.showResoursesFragment()
    }

    override fun onNavMapOption() {
        view.showMapFragment()
    }

    override fun onDrawerSuggestOption() {
        view.showSuggestFragment()
    }

    override fun onDrawerConfigOption() {
        view.showConfigFragment()
    }

    override fun onDrawerProfileOption() {
        view.showProfileFragment()
    }

    override fun onDrawerContactOption() {
        view.showContactFragment()
    }

    override fun onNavUsersOption() {
        view.showUsersFragment()
    }

    override fun onNavScanOption() {
        view.showScanListFragment()
    }

    override fun onNavResoursesProviderOption() {
        view.showResoursesProviderFragment()

    }



}