package com.cleteci.redsolidaria.ui.fragments.infoService

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanNoUserContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(total: Int)
    }

    interface Presenter: BaseContract.Presenter<ScanNoUserContract.View> {
        //fun onDrawerOptionAboutClick()
        fun loadCategoryData(id:String)
        fun loadServiceData(id:String)
    }
}