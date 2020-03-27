package com.cleteci.redsolidaria.ui.fragments.resoursesOffered

import com.cleteci.redsolidaria.models.Resourse
import com.cleteci.redsolidaria.models.ResourseCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResoursesOfferedContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<ResourseCategory>)
    }

    interface Presenter: BaseContract.Presenter<ResoursesOfferedContract.View> {
        //fun onDrawerOptionAboutClick()
        fun getData()
    }
}