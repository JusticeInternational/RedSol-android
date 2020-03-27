package com.cleteci.redsolidaria.ui.fragments.createService

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class CreateServiceContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<CreateServiceContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}