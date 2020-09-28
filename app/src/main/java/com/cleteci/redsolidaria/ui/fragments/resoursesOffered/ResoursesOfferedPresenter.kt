package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.ResourceCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResoursesOfferedPresenter : ResoursesOfferedContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResoursesOfferedContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResoursesOfferedContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun getData() {
        val arrayList = ArrayList<ResourceCategory>()//Creating an empty arraylist
        val tipo1 = ResourceCategory("1", "Salud",  "asdf", R.drawable.ic_emergency)//Creating an empty arraylist
        arrayList.add(tipo1)//Adding object in arraylist

        val tipo2 =
            ResourceCategory("2", "Educación",  "asdf", R.drawable.ic_education)//Creating an empty arraylist
        arrayList.add(tipo2)//Adding object in arraylist

        view.loadDataSuccess(arrayList)
    }


}