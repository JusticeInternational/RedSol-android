package com.cleteci.redsolidaria.ui.fragments.users

import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.models.User
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class AttendersListContract {

    interface View: BaseContract.View {
        fun init()
        fun showUsers(users: List<User>)
    }

    interface Presenter: BaseContract.Presenter<AttendersListContract.View> {
        fun loadDataService(serviceId:String, type:Int)
        fun loadDataCategory(categoryId:String, type:Int)


    }
}