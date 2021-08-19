package com.cleteci.redsolidaria.ui.fragments.welcome


import android.content.Intent
import android.os.Bundle

import android.view.*
import android.widget.Button
import android.widget.TextView
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment

import javax.inject.Inject

class WelcomeFragment : BaseFragment(), WelcomeContract.View {

    var tvRegister: TextView? = null
    var btLogin: Button? = null
    var btContinue: Button? = null


    @Inject
    lateinit var presenter: WelcomeContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): WelcomeFragment {
        return WelcomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false)

        tvRegister = rootView.findViewById(R.id.tvRegister)

        btLogin = rootView.findViewById(R.id.btLogin)

        btContinue = rootView.findViewById(R.id.btContinue)

        btContinue!!.setOnClickListener{
            BaseApp.sharedPreferences.loginLater=true

            BaseApp.sharedPreferences.isProviderService = false

            val intent = Intent(activity, MainActivity::class.java)

            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            activity!!.finish()


        }

        btLogin!!.setOnClickListener{

            (activity as LoginActivity).openLogin()

        }

        tvRegister?.setOnClickListener{

            (activity as LoginActivity).openRegisterActivity()

        }



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()

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


    companion object {
        val TAG: String = "WelcomeFragment"
    }
}