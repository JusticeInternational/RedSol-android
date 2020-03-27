package com.cleteci.redsolidaria.ui.fragments.basicsearch

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.ResourseCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class BasicSearchPresenter: BasicSearchContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: BasicSearchContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: BasicSearchContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadData() {

        //view.loadDataSuccess()
        val arrayList = ArrayList<ResourseCategory>()//Creating an empty arraylist
        val tipo1 = ResourseCategory(1,"Salud", R.drawable.ic_emergency)//Creating an empty arraylist
        arrayList.add(tipo1)//Adding object in arraylist
        val tipo2 = ResourseCategory(2,"Educacion",R.drawable.ic_education)//Creating an empty arraylist
        arrayList.add(tipo2)//Adding object in arraylist
        val tipo3 = ResourseCategory(3,"Trabajo",R.drawable.ic_job)//Creating an empty arraylist
        arrayList.add(tipo3)//Adding object in arraylist
        val tipo4 = ResourseCategory(4,"Transporte",R.drawable.ic_transp)//Creating an empty arraylist
        arrayList.add(tipo4)//Adding object in arraylist
        val tipo5 = ResourseCategory(5,"Comida",R.drawable.ic_food)//Creating an empty arraylist
        arrayList.add(tipo5)//Adding object in arraylist
        val tipo6 = ResourseCategory(6,"Asesoria",R.drawable.ic_justice)//Creating an empty arraylist
        arrayList.add(tipo6)//Adding object in arraylist

        view.loadDataSuccess(arrayList)
    }


}