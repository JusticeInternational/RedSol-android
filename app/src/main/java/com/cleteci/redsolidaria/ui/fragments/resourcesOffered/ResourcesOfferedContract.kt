package com.cleteci.redsolidaria.ui.fragments.resourcesOffered

import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.base.BaseContract

class ResourcesOfferedContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(pending: List<Category>, services:  List<Service>, genericServices:  List<Service>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun getData()
    }
}