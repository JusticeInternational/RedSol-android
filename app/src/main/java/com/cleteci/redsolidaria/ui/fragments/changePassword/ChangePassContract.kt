package com.cleteci.redsolidaria.ui.fragments.changePassword

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ChangePassContract {

    interface View: BaseContract.View {
        fun init()
        fun errorPass(mdg:String)
    }

    interface Presenter: BaseContract.Presenter<ChangePassContract.View> {
        //fun onDrawerOptionAboutClick()
        fun verifyData(newPass:String)
    }
}