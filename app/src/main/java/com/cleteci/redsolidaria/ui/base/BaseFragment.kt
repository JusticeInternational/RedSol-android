package com.cleteci.redsolidaria.ui.base

import android.content.Context
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    private var mFragmentNavigation: FragmentNavigation? = null

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