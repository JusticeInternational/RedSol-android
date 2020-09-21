package com.cleteci.redsolidaria.ui.fragments.resourcesByCity

import com.cleteci.redsolidaria.models.Resourse
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResourcesByCityContract {

    interface View: BaseContract.View {
        fun init()

        fun loadDataSuccess(list: List<Resourse>)
    }

    interface Presenter: BaseContract.Presenter<ResourcesByCityContract.View> {
        //fun onDrawerOptionAboutClick()
        fun loadData()
    }
}