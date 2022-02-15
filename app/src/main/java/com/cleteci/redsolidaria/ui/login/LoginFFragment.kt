package com.cleteci.redsolidaria.ui.login


import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel.QueryStatus
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFFragment : BaseFragment() {

    private val userAccountVM by viewModel<UserAccountViewModel>()
    private val organizationVM by viewModel<OrganizationViewModel>()

    fun newInstance(): LoginFFragment {
        return LoginFFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userAccountVM.status.observe(this, androidx.lifecycle.Observer { status: QueryStatus? ->
            when (status) {
                QueryStatus.NOTIFY_LOADING -> showLoading(true)
                QueryStatus.NOTIFY_SUCCESS -> {
                    if (BaseApp.sharedPreferences.isProviderService) {
                        BaseApp.sharedPreferences.userSaved?.let {
                            organizationVM.getOrganizationByUserId(it)
                        }
                    } else {
                        showLoading(false)
                        (activity as LoginActivity).openMainActivity()
                    }
                }
                QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.wrong_login))
                    showLoading(false)
                }
                QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
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
                QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                    showLoading(false)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btFacebook!!.setOnClickListener {
           // (activity as LoginActivity).loginFacebook()
            Toast.makeText(context,getString(R.string.in_build), Toast.LENGTH_LONG).show()

        }

        btGoogle!!.setOnClickListener {
            //(activity as LoginActivity).signInGoogle()
            Toast.makeText(context,getString(R.string.in_build), Toast.LENGTH_LONG).show()

        }
        etPass.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity?.let { hideSoftKeyboard(it) }
                validateEmailPass(etUser!!.text.toString(), etPass!!.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
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
        val email = emailStr.trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.wrong_email))
        } else if (pass.isEmpty()) {
            showError(getString(R.string.wrong_pass))
        } else {
            userAccountVM.login(email, pass)
        }
    }

    private fun hideSoftKeyboard(activity: FragmentActivity?) {
        if (null != activity && null != activity.window) {
            val viewFocus = activity.window.currentFocus
            if (null != viewFocus) {
                val inputMethodManager = BaseApp.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(viewFocus.windowToken, 0)
            }
        }
    }

    companion object {
        const val TAG: String = "LoginFFragment"
    }
}