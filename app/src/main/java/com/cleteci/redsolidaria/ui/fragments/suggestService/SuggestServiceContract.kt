package com.cleteci.redsolidaria.ui.fragments.suggestService

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class SuggestServiceContract {

    interface View: BaseContract.View {
        fun init()
    }

    interface Presenter: BaseContract.Presenter<SuggestServiceContract.View> {
        //fun onDrawerOptionAboutClick()
    }
}