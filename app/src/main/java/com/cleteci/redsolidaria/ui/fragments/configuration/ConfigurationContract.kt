package com.cleteci.redsolidaria.ui.fragments.configuration

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ConfigurationContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<ConfigurationContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}