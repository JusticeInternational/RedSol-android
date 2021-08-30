package com.cleteci.redsolidaria.di.module

import android.app.Activity
import com.cleteci.redsolidaria.ui.activities.main.MainContract
import com.cleteci.redsolidaria.ui.activities.main.MainPresenter
import com.cleteci.redsolidaria.ui.activities.register.RegisterContract
import com.cleteci.redsolidaria.ui.activities.register.RegisterPresenter
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordContract
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordPresenter
import com.cleteci.redsolidaria.ui.activities.splash.SplashContract
import com.cleteci.redsolidaria.ui.activities.splash.SplashPresenter

import dagger.Module
import dagger.Provides

/**
 * Created by ogulcan on 07/02/2018.
 */
@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): MainContract.Presenter {
        return MainPresenter()
    }

    @Provides
    fun provideSplashPresenter(): SplashContract.Presenter {
        return SplashPresenter()
    }

    @Provides
    fun provideRegisterPresenter(): RegisterContract.Presenter {
        return RegisterPresenter()
    }

    @Provides
    fun provideResetPasswordPresenter(): ResetPasswordContract.Presenter {
        return ResetPasswordPresenter()
    }

}