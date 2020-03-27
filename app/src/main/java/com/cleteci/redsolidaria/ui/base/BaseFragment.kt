package com.cleteci.redsolidaria.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment (){

    val ARGS_INSTANCE = "com.cleteci.redsolidaria"


    internal var mFragmentNavigation: FragmentNavigation?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
    }

    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment)
    }

}