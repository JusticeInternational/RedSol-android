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

import javax.inject.Inject

class LoginFFragment : BaseFragment(), LoginFContract.View {


    var btGoogle: Button? = null
    var btFacebook: Button? = null
    var tvRegister: TextView? = null
    var tvResetPass: TextView? = null
    var btLogin: Button? = null
    var etUser: EditText? = null
    var etPass: EditText? = null


    @Inject
    lateinit var presenter: LoginFContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): LoginFFragment {
        return LoginFFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_login, container, false)

        btFacebook = rootView.findViewById(R.id.btFacebook)

        btGoogle = rootView.findViewById(R.id.btGoogle)

        btLogin = rootView.findViewById(R.id.btLogin)

        tvRegister = rootView.findViewById(R.id.tvRegister)

        etUser = rootView?.findViewById(R.id.etUser)
        etPass = rootView.findViewById(R.id.etPass)

        tvResetPass = rootView?.findViewById(R.id.tvResetPass)

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


    override fun validEmailPass() {
        (activity as LoginActivity).loginEmailPass(etUser?.text.toString())
    }

    override fun errorEmailPass(mdg: String) {
        Toast.makeText(activity, mdg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG: String = "LoginFFragment"

    }


}