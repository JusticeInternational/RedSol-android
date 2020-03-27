package com.cleteci.redsolidaria.ui.fragments.resourcesByCity

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resourse
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResourcesByCityPresenter: ResourcesByCityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResourcesByCityContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResourcesByCityContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun loadData() {

        //view.loadDataSuccess()
        val arrayList = ArrayList<Resourse>()//Creating an empty arraylist
        val tipo1 = Resourse(1, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo1)//Adding object in arraylist

        val tipo2 = Resourse(2, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo2)//Adding object in arraylist

        val tipo3 = Resourse(3, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo3)//Adding object in arraylist

        val tipo4 = Resourse(4, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo4)//Adding object in arraylistval tipo2 =

        val tipo5 = Resourse(5, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo5)//Adding object in arra

        val tipo6 = Resourse(6, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo6)//Adding object in arraylist

        val tipo7 = Resourse(7, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo7)//Adding object in arraylist

        val tipo8 = Resourse(8, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo8)//Adding object in arraylist

        val tipo9 = Resourse(9, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo9)//Adding object in arraylist

        val tipo10 = Resourse(10, "Justice International","Salud",  R.drawable.ic_sun2)//Creating an empty arraylist
        arrayList.add(tipo10)//Adding object in arraylist




        view.loadDataSuccess(arrayList)
    }


}