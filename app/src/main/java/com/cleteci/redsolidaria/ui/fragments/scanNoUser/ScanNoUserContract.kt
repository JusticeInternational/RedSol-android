package com.cleteci.redsolidaria.ui.fragments.infoService

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class ScanNoUserContract {

    interface View: BaseContract.View {
        fun init()
        fun loadDataSuccess(msg: String)
        fun loadDataError(msg: String)
    }

    interface Presenter: BaseContract.Presenter<ScanNoUserContract.View> {
        //fun onDrawerOptionAboutClick()
       fun atentionUnregisteredCategory(orgID:String, categoryID:String, name: String, lastName: String, identification: String?, gender: Int?, country: String?, age: Int?, phone: String?, email: String?, otherInfo: String?,  nameCat:String?)

        fun atentionUnregisteredService(orgID:String, serviceID:String, name: String, lastName: String, identification: String?, gender: Int?, country: String?, age: Int?, phone: String?, email: String?, otherInfo: String?, nameService:String?,
                                        isGeneric:Boolean)


        fun validateAtentionUnregisteredCategory(orgID:String, categoryID:String, name: String?, lastName: String?, identification: String?, gender: Int?, country: String?, age: Int?, phone: String?, email: String?, otherInfo: String?,  nameCat:String?)

        fun validateAtentionUnregisteredService(orgID:String, serviceID:String, name: String?, lastName: String?, identification: String?, gender: Int?, country: String?, age: Int?, phone: String?, email: String?, otherInfo: String?, nameService:String?,
                                        isGeneric:Boolean)
    }
}