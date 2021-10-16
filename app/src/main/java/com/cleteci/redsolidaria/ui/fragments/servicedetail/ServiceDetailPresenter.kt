package com.cleteci.redsolidaria.ui.fragments.servicedetail

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
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

    override fun getData() : ArrayList<Category> {
        val listCategory = ArrayList<Category>()
        val tipo1 = Category("1", "Salud", R.drawable.ic_emergency)
        listCategory.add(tipo1)
        val tipo2 = Category("2", "Educacion", R.drawable.ic_education)
        listCategory.add(tipo2)
        val tipo3 = Category("3", "Trabajo", R.drawable.ic_job)
        listCategory.add(tipo3)
        val tipo4 = Category("4", "Transporte", R.drawable.ic_transp)
        listCategory.add(tipo4)
        val tipo5 = Category("5", "Comida", R.drawable.ic_food)
        listCategory.add(tipo5)
        val tipo6 = Category("6", "Asesoria", R.drawable.ic_legal)
        listCategory.add(tipo6)

        return listCategory
    }


}