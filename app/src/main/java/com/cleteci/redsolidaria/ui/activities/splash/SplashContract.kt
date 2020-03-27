package com.cleteci.redsolidaria.ui.activities.splash

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class SplashContract {

    interface View: BaseContract.View {
        fun init()

    }

    interface Presenter: BaseContract.Presenter<SplashContract.View> {

       // fun onDrawerContactOption()


    }
}