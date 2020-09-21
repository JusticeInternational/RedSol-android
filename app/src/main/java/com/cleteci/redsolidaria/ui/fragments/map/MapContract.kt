package com.cleteci.redsolidaria.ui.fragments.map

import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class MapContract {

    interface View: BaseContract.View {
        fun init()

        fun loadDataSuccess(list: List<ResourceCategory>)
    }

    interface Presenter: BaseContract.Presenter<MapContract.View> {


        fun loadData()
    }
}