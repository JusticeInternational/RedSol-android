package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResoursesOfferedContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<ResourceCategory>)
    }

    interface Presenter: BaseContract.Presenter<ResoursesOfferedContract.View> {
        //fun onDrawerOptionAboutClick()
        fun getData()
    }
}