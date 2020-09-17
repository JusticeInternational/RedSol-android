package com.cleteci.redsolidaria.ui.activities.resetPassword

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResetPasswordContract {

    interface View : BaseContract.View {
        fun init()

        fun askCode()

        fun goToLogin()

        fun showError(msg: String)


    }

    interface Presenter : BaseContract.Presenter<ResetPasswordContract.View> {

        fun validateRegister(email: String)

    }
}