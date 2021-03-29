package com.cleteci.redsolidaria.ui.fragments.basicsearch

import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class BasicSearchContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(list: List<Category>)
    }

    interface Presenter: BaseContract.Presenter<BasicSearchContract.View> {
        fun loadData()
    }
}