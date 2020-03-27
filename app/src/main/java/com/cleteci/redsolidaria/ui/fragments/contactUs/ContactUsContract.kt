package com.cleteci.redsolidaria.ui.fragments.contactUs

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ContactUsContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<ContactUsContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}