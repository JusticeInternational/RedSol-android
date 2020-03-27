package com.cleteci.redsolidaria.di.component

import com.cleteci.redsolidaria.di.module.ActivityModule
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.activities.register.RegisterActivity
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordActivity
import com.cleteci.redsolidaria.ui.activities.splash.SplashActivity
import dagger.Component

/**
 * Created by ogulcan on 07/02/2018.
 */
@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(loginActivity: LoginActivity)

    fun inject(registerActivity: RegisterActivity)

    fun inject(resetPasswordActivity: ResetPasswordActivity)

}