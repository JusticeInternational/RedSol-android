package com.cleteci.redsolidaria.ui.fragments.map

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MapPresenter: MapContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MapContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MapContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadData() {

        val arrayList = ArrayList<Category>()//Creating an empty arraylist
        val tipo1 = Category("1","Salud",  R.drawable.ic_emergency)//Creating an empty arraylist
        arrayList.add(tipo1)//Adding object in arraylist
        val tipo2 = Category("2","Educacion",  R.drawable.ic_education)//Creating an empty arraylist
        arrayList.add(tipo2)//Adding object in arraylist
        val tipo3 = Category("3","Trabajo", R.drawable.ic_job)//Creating an empty arraylist
        arrayList.add(tipo3)//Adding object in arraylist
        val tipo4 = Category("4","Transporte",  R.drawable.ic_transp)//Creating an empty arraylist
        arrayList.add(tipo4)//Adding object in arraylist
        val tipo6 = Category("6","Asesoria",  R.drawable.ic_justice)//Creating an empty arraylist
        arrayList.add(tipo6)//Adding object in arraylist

        view.loadDataSuccess(arrayList)
    }


}