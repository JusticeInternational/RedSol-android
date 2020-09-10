package com.cleteci.redsolidaria

import android.app.Application
import android.content.Context

import com.cleteci.redsolidaria.di.component.ApplicationComponent
import com.cleteci.redsolidaria.di.component.DaggerApplicationComponent
import com.cleteci.redsolidaria.di.module.ApplicationModule
import com.cleteci.redsolidaria.util.Prefs
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

/**
 * Created by ogulcan on 07/02/2018.
 */
class BaseApp : Application() {


    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()

        if (BuildConfig.DEBUG) {

        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        prefs = Prefs(applicationContext)
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }



    companion object {
        lateinit var instance: BaseApp private set
        lateinit var prefs: Prefs
    }
}