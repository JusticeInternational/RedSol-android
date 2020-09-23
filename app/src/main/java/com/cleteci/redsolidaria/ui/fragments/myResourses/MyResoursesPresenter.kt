package com.cleteci.redsolidaria.ui.fragments.myResourses

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resource
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
        val arrayList = ArrayList<Resource>()//Creating an empty arraylist
        val tipo1 = Resource("1","Justice International",  hourHand = "3:00 a 4:00", ranking = "1", cate = "Salud", photo = R.drawable.ic_emergency)//Creating an empty arraylist
        arrayList.add(tipo1)//Adding object in arraylist

        val tipo2 = Resource("2", "Justice International",  hourHand = "3:00 a 4:00", ranking = "3", cate = "Educacion", photo = R.drawable.ic_education)//Creating an empty arraylist
        arrayList.add(tipo2)//Adding object in arraylist


        view.loadDataSuccess(arrayList,arrayList,arrayList,arrayList)
    }


}