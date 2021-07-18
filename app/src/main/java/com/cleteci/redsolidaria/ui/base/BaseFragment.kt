package com.cleteci.redsolidaria.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment (){

    private var mFragmentNavigation: FragmentNavigation? = null

    inline fun <T : Fragment> T.withArguments(capacity: Int, block: Bundle.() -> Unit): T {
        arguments = Bundle(capacity).apply(block)
        return this
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