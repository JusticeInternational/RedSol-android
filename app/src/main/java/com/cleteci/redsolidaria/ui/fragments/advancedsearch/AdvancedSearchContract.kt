package com.cleteci.redsolidaria.ui.fragments.advancedsearch

import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class AdvancedSearchContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(list: List<ResourceCategory>)
    }

    interface Presenter: BaseContract.Presenter<AdvancedSearchContract.View> {
        //fun onDrawerOptionAboutClick()
        fun search(data: String)
    }
}