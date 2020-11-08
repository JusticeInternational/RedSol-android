package com.cleteci.redsolidaria.ui.fragments.servicedetail

import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ServiceDetailContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<ServiceDetailContract.View> {
        fun getData():ArrayList<ResourceCategory>
    }
}