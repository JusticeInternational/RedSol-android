package com.cleteci.redsolidaria.ui.fragments.infoService

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class InfoServiceContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<InfoServiceContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}