package com.cleteci.redsolidaria.ui.activities.resetPassword

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
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity

import com.github.glomadrian.codeinputlib.CodeInput
import java.util.*


/**
 * Created by ogulcan on 07/02/2018.
 */
class ResetPasswordActivity : AppCompatActivity(), ResetPasswordContract.View {


    var lyEmail: RelativeLayout? = null

    var lyVerify: RelativeLayout? = null

    var etEmail: EditText? = null

    var btSend: Button? = null

    var btCancel: Button? = null

    var btLogin: Button? = null


    @Inject
    lateinit var presenter: ResetPasswordContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
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


        lyEmail = findViewById(R.id.lyEmail)

        lyVerify = findViewById(R.id.lyVerify)

        etEmail = findViewById(R.id.etEmail)

        btSend = findViewById(R.id.btSend)

        btCancel = findViewById(R.id.btCancel)

        btLogin = findViewById(R.id.btLogin)



        btSend?.setOnClickListener {

            presenter.validateRegister(etEmail?.text.toString())

        }
        btLogin?.setOnClickListener {
            goToLogin()

        }

        btCancel?.setOnClickListener {
            finish()

        }


    }

    override fun askCode() {
        runOnUiThread(Runnable {
            lyEmail?.visibility = View.GONE

            lyVerify?.visibility = View.VISIBLE
        })
    }

    override fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
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

    override fun showError(msg: String) {
        var toast: Toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
        var view: View = toast.getView()
        var view1: TextView = view.findViewById(android.R.id.message)
        view1.setPadding(10, 2, 10, 2)
        view1.setTextColor(Color.WHITE)
        view.setBackgroundResource(R.drawable.bg_color_red_round_corner)
        toast.show()
    }

}