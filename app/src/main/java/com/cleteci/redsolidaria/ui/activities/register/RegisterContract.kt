package com.cleteci.redsolidaria.ui.activities.register

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class RegisterContract {

    interface View : BaseContract.View {
        fun init()

        fun askCode()

        fun emailExists()

        fun goToLogin()

        fun tryAgain()

        fun showError(msg:String)

    }

    interface Presenter : BaseContract.Presenter<RegisterContract.View> {

        fun validateRegister(term:Boolean, policies:Boolean, name: String, lastName: String, email: String, pass:String)

        fun receiveUser()

        fun goToRegister()

    }
}