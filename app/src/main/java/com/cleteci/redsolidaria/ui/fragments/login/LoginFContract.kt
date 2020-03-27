package com.cleteci.redsolidaria.ui.fragments.login

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class LoginFContract {

    interface View: BaseContract.View {
        fun init()
        fun validEmailPass()
        fun errorEmailPass(mdg:String)
    }

    interface Presenter: BaseContract.Presenter<LoginFContract.View> {
        fun validateEmailPass(email:String, pass:String)
    }
}