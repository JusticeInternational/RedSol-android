package com.cleteci.redsolidaria.ui.activities.login

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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.activities.register.RegisterActivity
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordActivity
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

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import java.util.*


/**
 *
 */
class LoginActivity : AppCompatActivity(), LoginContract.View {


    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    var callbackManager: CallbackManager? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private var lyContainerLogin: RelativeLayout? = null

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        injectDependency()
        configureGoogleSignIn()
        firebaseAuth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })

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

        lyContainerLogin = findViewById(R.id.lyContainerLogin)

        if (BaseApp.prefs.login_later) {
            val fragment = LoginFFragment().newInstance() as Fragment
            val fragmentManager = getSupportFragmentManager()
            fragmentManager.beginTransaction().replace(R.id.lyContainerLogin, fragment).commit()
        } else {
            val fragment = WelcomeFragment().newInstance() as Fragment
            val fragmentManager = getSupportFragmentManager()
            fragmentManager.beginTransaction().replace(R.id.lyContainerLogin, fragment).commit()
        }
    }

    fun openLogin() {
        val fragment = LoginFFragment().newInstance() as Fragment
        val fragmentManager = getSupportFragmentManager()
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.lyContainerLogin, fragment, LoginFFragment.TAG).commit()
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                //Toast.makeText(this, "LOGEO EXITOSO", Toast.LENGTH_LONG).show()
                BaseApp.prefs.login_later = false
                BaseApp.prefs.user_saved = acct.displayName
                BaseApp.prefs.is_provider_service = false
                openMainActivity()
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }


    fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()

    }

    fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)

        startActivity(intent)

    }

    fun openResetActivity() {
        val intent = Intent(this, ResetPasswordActivity::class.java)

        startActivity(intent)

    }


    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = firebaseAuth.currentUser
                    BaseApp.prefs.login_later = false
                    BaseApp.prefs.user_saved = user!!.displayName
                    BaseApp.prefs.is_provider_service = false
                    openMainActivity()
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
    }

    fun loginEmailPass(user: String) {

        BaseApp.prefs.login_later = false
        BaseApp.prefs.user_saved = "USER CUSTOM"
        user.replace(" ", "");

        if (user.equals("hola@gmail.com") || user.equals("hola@gmail.com ")) {//simulating if it is service provider mail
            BaseApp.prefs.is_provider_service = true
        } else {
            BaseApp.prefs.is_provider_service = false
        }
        openMainActivity()
    }


}