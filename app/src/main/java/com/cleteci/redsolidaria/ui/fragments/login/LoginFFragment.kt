package com.cleteci.redsolidaria.ui.fragments.login


import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.viewModels.BaseViewModel.QueryStatus
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel

import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

import javax.inject.Inject


class LoginFFragment : BaseFragment(), LoginFContract.View {

    private val userAccountVM by viewModel<UserAccountViewModel>()
    private val organizationVM by viewModel<OrganizationViewModel>()

    @Inject
    lateinit var presenter: LoginFContract.Presenter

    fun newInstance(): LoginFFragment {
        return LoginFFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        userAccountVM.status.observe(this, androidx.lifecycle.Observer { status: QueryStatus? ->
            when (status) {
                QueryStatus.NOTIFY_LOADING -> showLoading(true)
                QueryStatus.NOTIFY_SUCCESS -> {
                    if (BaseApp.prefs.is_provider_service) {
                        BaseApp.prefs.user_saved?.let { organizationVM.getOrganization(it) }
                    } else {
                        showLoading(false)
                        (activity as LoginActivity).openMainActivity()
                    }
                }
                QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.wrong_login))
                    showLoading(false)
                }
                else -> {
                    showError(getString(R.string.wrong_login))
                    showLoading(false)
                }
            }
        })

        organizationVM.status.observe(this, androidx.lifecycle.Observer { status: QueryStatus? ->
            when (status) {
                QueryStatus.NOTIFY_LOADING -> showLoading(true)
                QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                    (activity as LoginActivity).openMainActivity()
                }
                QueryStatus.ORGANIZATION_NOT_FOUND -> {
                    showError(getString(R.string.error_organization_not_found))
                    showLoading(false)
                    (activity as LoginActivity).openMainActivity()
                }
                QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.error_getting_organization))
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if(show) View.VISIBLE else View.GONE
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
           validateEmailPass(etUser!!.text.toString(), etPass!!.text.toString())
        }

        tvRegister!!.setOnClickListener {
            (activity as LoginActivity).openRegisterActivity()
        }

        tvResetPass!!.setOnClickListener {
            (activity as LoginActivity).openResetActivity()
        }
    }

    private fun validateEmailPass(emailStr: String, pass: String) {
        val email= emailStr.trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.wrong_email))
        } else if (pass.isEmpty()) {
            showError(getString(R.string.wrong_pass))
        } else {
            userAccountVM.login(email, pass)
        }
    }

    override fun validEmailPass() {
        (activity as LoginActivity).loginEmailPass()
    }

    override fun showError(msg: String) {
        activity?.runOnUiThread {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG: String = "LoginFFragment"
    }
}