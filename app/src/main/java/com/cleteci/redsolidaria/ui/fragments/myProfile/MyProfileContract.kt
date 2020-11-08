package com.cleteci.redsolidaria.ui.fragments.myProfile

import android.graphics.Bitmap
import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfileContract {

    interface View: BaseContract.View {
        fun init()
        fun showQR(bitmap:Bitmap)
    }

    interface Presenter: BaseContract.Presenter<MyProfileContract.View> {
        fun getQR()
    }
}