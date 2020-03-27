package com.cleteci.redsolidaria.ui.activities.register

import android.os.Bundle


import androidx.appcompat.app.AppCompatActivity

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerActivityComponent

import com.cleteci.redsolidaria.di.module.ActivityModule

import uk.co.chrisjenx.calligraphy.CalligraphyConfig

import javax.inject.Inject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.fragments.login.LoginFFragment
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomeFragment
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.github.glomadrian.codeinputlib.CodeInput
import com.google.firebase.auth.FacebookAuthProvider
import java.util.*


/**
 * Created by ogulcan on 07/02/2018.
 */
class RegisterActivity : AppCompatActivity(), RegisterContract.View {


    var mScrollView: ScrollView? = null

    var etName: EditText? = null

    var etEmail: EditText? = null

    var etPass: EditText? = null

    var cbTerms: CheckBox? = null

    var cbPolicies: CheckBox? = null

    var btSend: Button? = null

    var lyVerify: RelativeLayout? = null

    var btLogin: Button? = null

    var codeInput: CodeInput? = null


    @Inject
    lateinit var presenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        injectDependency()

        presenter.attach(this)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }


    override fun init() {

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        mScrollView = findViewById(R.id.mScrollView)

        etName = findViewById(R.id.etName)

        etEmail = findViewById(R.id.etEmail)

        etPass = findViewById(R.id.etPass)

        cbTerms = findViewById(R.id.cbTerms)

        cbPolicies = findViewById(R.id.cbPolicies)

        btSend = findViewById(R.id.btSend)

        lyVerify = findViewById(R.id.lyVerify)

        btLogin = findViewById(R.id.btLogin)

        codeInput = findViewById(R.id.codeInput)

        btSend?.setOnClickListener {

            presenter.validateRegister(
                cbTerms!!.isChecked,
                cbPolicies!!.isChecked,
                etName!!.text.toString(),
                etEmail!!.text.toString(),
                etPass!!.text.toString()
            )

        }
        btLogin?.setOnClickListener {

            presenter.validatecode(Arrays.toString(codeInput?.code))

        }


    }

    override fun askCode() {
        mScrollView?.visibility = View.GONE

        lyVerify?.visibility = View.VISIBLE
    }

    override fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
    }

    override fun showError(msg: String) {
        var toast: Toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
        //toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT,0,0)
        var view: View = toast.getView()
        var view1: TextView = view.findViewById(android.R.id.message)
        view1.setPadding(10, 2, 10, 2)
        view1.setTextColor(Color.WHITE)
        view.setBackgroundResource(R.drawable.bg_color_red_round_corner)
        toast.show()
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            android.R.id.home -> {
                finish();
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}