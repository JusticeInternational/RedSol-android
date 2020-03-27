package com.cleteci.redsolidaria.ui.activities.resetPassword

import android.util.Log
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class ResetPasswordPresenter : ResetPasswordContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ResetPasswordContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ResetPasswordContract.View) {
        this.view = view
        view.init() // as default
    }


    override fun validateRegister(email: String) {

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.replace(" ", "")).matches()) {
            view.showError("Introduzca un correo válido")
        } else {
            view.askCode()

        }
    }


}