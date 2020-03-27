package com.cleteci.redsolidaria.ui.fragments.myResourses

import com.cleteci.redsolidaria.models.Resourse
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyResoursesContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<Resourse>, saved: List<Resourse>, volunteer: List<Resourse>, used: List<Resourse>)
    }

    interface Presenter: BaseContract.Presenter<MyResoursesContract.View> {
        fun getData()
    }
}