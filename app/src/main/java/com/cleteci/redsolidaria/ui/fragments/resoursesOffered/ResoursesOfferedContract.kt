package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResoursesOfferedContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<Category>, services:  List<Service>, genericServices:  List<Service>)
    }

    interface Presenter: BaseContract.Presenter<ResoursesOfferedContract.View> {
        //fun onDrawerOptionAboutClick()
        fun getData()
    }
}