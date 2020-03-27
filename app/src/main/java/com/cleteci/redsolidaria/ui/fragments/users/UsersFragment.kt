package com.cleteci.redsolidaria.ui.fragments.users


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject


class UsersFragment : BaseFragment() , UsersContract.View  {


    @Inject lateinit var presenter: UsersContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): UsersFragment {
        return UsersFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_users, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        aboutComponent.inject(this)
    }
    override fun init() {

    }

    private fun initView() {
        //presenter.loadMessage()
    }

    companion object {
        val TAG: String = "UsersFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.users).toString(),activity!!.resources.getColor(R.color.colorWhite))

    }



}
