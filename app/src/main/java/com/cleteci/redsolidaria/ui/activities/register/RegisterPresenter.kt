package com.cleteci.redsolidaria.ui.activities.register

import android.util.Log
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class RegisterPresenter : RegisterContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: RegisterContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RegisterContract.View) {
        this.view = view
        view.init() // as default
    }


    override fun validateRegister(term: Boolean, policies: Boolean, name: String, email: String, pass: String) {
        if (!term) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.accept_term_condition))
        } else if (!policies) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.accept_privacy_policies))
        } else if (name.isEmpty() || name.length < 1) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_name))
        } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.replace(" ", "")).matches()) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_email))
        } else if (pass.isEmpty() || pass.length < 4) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_pass))
        } else {
            view.askCode()
        }
    }

    override fun validatecode(code: String) {
        if (code.length != 18) {
            view.showError(BaseApp.instance.applicationContext!!.getString(R.string.enter_valid_code))

        } else {
            view.goToLogin()
        }
    }


}