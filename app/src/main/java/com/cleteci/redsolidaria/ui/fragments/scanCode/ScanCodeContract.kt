package com.cleteci.redsolidaria.ui.fragments.scanCode

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanCodeContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<ScanCodeContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}