package com.cleteci.redsolidaria.ui.fragments.myResources

import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.base.BaseContract

class MyResourcesContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<Service>, saved: List<Service>, volunteer: List<Service>, used: List<Service>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun getData()
    }
}