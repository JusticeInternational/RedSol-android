package com.cleteci.redsolidaria.ui.search

import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.base.BaseContract


class SearchContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(list: List<Category>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun loadData()
    }
}