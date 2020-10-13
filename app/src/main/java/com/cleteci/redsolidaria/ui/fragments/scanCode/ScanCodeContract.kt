package com.cleteci.redsolidaria.ui.fragments.scanCode

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanCodeContract {

    interface View: BaseContract.View {
        fun init()
        fun showErrorMsg(msg: String)
        fun showSuccessMsg(msg: String)

    }

    interface Presenter: BaseContract.Presenter<ScanCodeContract.View> {
        fun countService(userid: String, serviceid:String)
        fun countCategory(userid: String, categoriyid:String)
    }
}