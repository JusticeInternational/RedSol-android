package com.cleteci.redsolidaria.ui.register

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleteci.redsolidaria.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.ui.login.LoginActivity
import com.cleteci.redsolidaria.util.Constants.Companion.DEFAULT_PASSWORD_LENGTH
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btSend
import kotlinx.android.synthetic.main.activity_register.etEmail
import kotlinx.android.synthetic.main.activity_register.etLastName
import kotlinx.android.synthetic.main.activity_register.etName
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val userAccountVM by viewModel<UserAccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userAccountVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                    val userName = etName.text.toString() + " " + etLastName.text.toString()
                    showAlert(R.drawable.ic_check_green,
                        getString(R.string.welcome_success, userName),
                        getString(R.string.go_to_login))
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showLoading(false)
                    showAlert(R.drawable.ic_error,
                        getString(R.string.error_creating_user),
                        getString(R.string.ok))
                }
                BaseViewModel.QueryStatus.EMAIL_ALREADY_REGISTERED -> {
                    showLoading(false)
                    showAlert(R.drawable.ic_error,
                        getString(R.string.error_creating_user_email_exist),
                        getString(R.string.ok))
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showLoading(false)
                    showInfoDialog(
                        this,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                }
                else -> {
                    showLoading(false)
                }
            }
        })

        init()
    }

    fun init() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btSend.setOnClickListener {
            validateRegister(
                cbTerms.isChecked,
                cbPolicies.isChecked,
                etName.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                etPass.text.toString()
            )
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showError(msg: String) {
        val toast: Toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        val view = toast.view
        val textView: TextView = view.findViewById(android.R.id.message)
        textView.setPadding(10, 2, 10, 2)
        textView.setTextColor(Color.WHITE)
        view.setBackgroundResource(R.drawable.bg_color_red_round_corner)
        toast.show()
    }

    private fun showLoading(show: Boolean) {
        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showAlert(icon: Int, msg: String, buttonText: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)

        val ivIcon = dialog.findViewById(R.id.ivIcon) as ImageView
        val tvAlertMsg = dialog.findViewById(R.id.tvAlertMsg) as TextView
        ivIcon.setImageResource(icon)
        tvAlertMsg.text = msg
        val yesBtn = dialog.findViewById(R.id.btCont) as Button

        yesBtn.text = buttonText
        yesBtn.setOnClickListener {
            if (buttonText == getString(R.string.go_to_login)) {
                goToLogin()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validateRegister(term: Boolean, policies: Boolean, name: String, lastName: String, email: String, password: String) {
        if (name.isEmpty()) {
            showError(getString(R.string.enter_valid_name))
        } else if (lastName.isEmpty()) {
            showError(getString(R.string.enter_valid_last_name))
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            showError(getString(R.string.enter_valid_email))
        } else if (password.isEmpty() || password.length < DEFAULT_PASSWORD_LENGTH) {
            showError(getString(R.string.enter_valid_pass))
        } else if (!term) {
            showError(getString(R.string.accept_term_condition))
        } else if (!policies) {
            showError(getString(R.string.accept_privacy_policies))
        } else {
            userAccountVM.createUser(name, lastName, email, password)
        }
    }

}