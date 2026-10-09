package com.cleteci.redsolidaria.ui.fragments.createService

import com.cleteci.redsolidaria.ui.base.BaseContract

class CreateServiceContract {

    interface View: BaseContract.View {
        fun init()
        fun onServiceLoading(show: Boolean)
        fun onServiceCreatedSuccess(msg: String)
        fun onServiceCreatedError(msg: String)
    }

    interface Presenter: BaseContract.Presenter<CreateServiceContract.View> {
        fun createService(
            name: String,
            description: String,
            isGeneral: Boolean,
            organizationId: String,
            serviceCategoryId: String
        )
    }
}
