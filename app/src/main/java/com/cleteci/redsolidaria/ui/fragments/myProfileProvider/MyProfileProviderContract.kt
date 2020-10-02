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
    }

    interface Presenter: BaseContract.Presenter<MyProfileProviderContract.View> {
        //fun onDrawerOptionAboutClick()
        fun loadData()
    }
}