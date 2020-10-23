package com.cleteci.redsolidaria.ui.fragments.users

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<UsersContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}