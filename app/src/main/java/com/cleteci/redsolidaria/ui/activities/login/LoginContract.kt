package com.cleteci.redsolidaria.ui.activities.login

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class LoginContract {

    interface View: BaseContract.View {
        fun init()

    }

    interface Presenter: BaseContract.Presenter<LoginContract.View> {



    }
}