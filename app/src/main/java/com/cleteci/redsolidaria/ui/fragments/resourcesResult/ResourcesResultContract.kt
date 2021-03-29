package com.cleteci.redsolidaria.ui.fragments.resourcesResult

import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResourcesResultContract {

    interface View: BaseContract.View {
        fun init()

        fun loadDataSuccess(list: List<Service>)
    }

    interface Presenter: BaseContract.Presenter<ResourcesResultContract.View> {
        //fun onDrawerOptionAboutClick()
        fun loadData(id: String, keyWord: String)
    }
}