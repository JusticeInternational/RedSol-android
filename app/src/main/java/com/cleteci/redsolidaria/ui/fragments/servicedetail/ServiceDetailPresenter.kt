package com.cleteci.redsolidaria.ui.fragments.servicedetail

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.ResourceCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ServiceDetailPresenter: ServiceDetailContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ServiceDetailContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ServiceDetailContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun getData() : ArrayList<ResourceCategory> {

        val listCategory = ArrayList<ResourceCategory>()
        val tipo1 = ResourceCategory("1", "Salud","Salud", R.drawable.ic_emergency, "")//Creating an empty arraylist
        listCategory.add(tipo1)//Adding object in arraylist
        val tipo2 = ResourceCategory("2", "Educacion","Salud", R.drawable.ic_education, "")//Creating an empty arraylist
        listCategory.add(tipo2)//Adding object in arraylist
        val tipo3 = ResourceCategory("3", "Trabajo","Salud", R.drawable.ic_job, "")//Creating an empty arraylist
        listCategory.add(tipo3)//Adding object in arraylist
        val tipo4 = ResourceCategory("4", "Transporte","Salud", R.drawable.ic_transp, "")//Creating an empty arraylist
        listCategory.add(tipo4)//Adding object in arraylist
        val tipo5 = ResourceCategory("5", "Comida","Salud", R.drawable.ic_food, "")//Creating an empty arraylist
        listCategory.add(tipo5)//Adding object in arraylist
        val tipo6 = ResourceCategory("6", "Asesoria","Salud", R.drawable.ic_justice, "")//Creating an empty arraylist
        listCategory.add(tipo6)//Adding object in arraylist

        return listCategory
    }


}