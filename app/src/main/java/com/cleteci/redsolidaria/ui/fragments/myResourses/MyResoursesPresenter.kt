package com.cleteci.redsolidaria.ui.fragments.myResourses

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Service
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyResoursesPresenter: MyResoursesContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyResoursesContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyResoursesContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun getData() {
        val arrayList = ArrayList<Service>()//Creating an empty arraylist

        view.loadDataSuccess(arrayList,arrayList,arrayList,arrayList)
    }


}