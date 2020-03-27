package com.cleteci.redsolidaria.ui.fragments.welcome

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class WelcomeContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<WelcomeContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}