package com.cleteci.redsolidaria.ui.fragments.login


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity

import com.cleteci.redsolidaria.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

import javax.inject.Inject




class LoginFFragment : BaseFragment(), LoginFContract.View {

    @Inject
    lateinit var presenter: LoginFContract.Presenter

    fun newInstance(): LoginFFragment {
        return LoginFFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_login, container, false)

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

    override fun init() {}

    private fun initView() {
        btFacebook!!.setOnClickListener {
            (activity as LoginActivity).loginFacebook()
        }

        btGoogle!!.setOnClickListener {
            (activity as LoginActivity).signInGoogle()
        }

        btLogin!!.setOnClickListener {
            presenter.validateEmailPass(etUser!!.text.toString(), etPass!!.text.toString())
        }

        tvRegister!!.setOnClickListener {
            (activity as LoginActivity).openRegisterActivity()
        }

        tvResetPass!!.setOnClickListener {
            (activity as LoginActivity).openResetActivity()
        }
    }

    override fun validEmailPass() {
        (activity as LoginActivity).loginEmailPass()
    }

    override fun errorEmailPass(mdg: String) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(activity, mdg, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        const val TAG: String = "LoginFFragment"
    }
}