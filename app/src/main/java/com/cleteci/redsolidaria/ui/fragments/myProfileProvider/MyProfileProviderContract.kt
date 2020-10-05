package com.cleteci.redsolidaria.ui.fragments.myProfileProvider

import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfileProviderContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(org: Organization)
        fun savedSuccess()
    }

    interface Presenter: BaseContract.Presenter<MyProfileProviderContract.View> {
        //fun onDrawerOptionAboutClick()
        fun loadData()
        fun updateOrg(name: String, webPage: String, phone: String, aboutUs: String, servDesc: String)
    }
}