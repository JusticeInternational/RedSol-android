package com.cleteci.redsolidaria

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient

import com.cleteci.redsolidaria.di.component.ApplicationComponent
import com.cleteci.redsolidaria.di.component.DaggerApplicationComponent
import com.cleteci.redsolidaria.di.module.ApplicationModule
import com.cleteci.redsolidaria.di.modules.appModule
import com.cleteci.redsolidaria.di.modules.controllersModule
import com.cleteci.redsolidaria.di.modules.viewModelsModule
import com.cleteci.redsolidaria.util.Constants
import com.cleteci.redsolidaria.util.Prefs
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import org.koin.android.ext.android.startKoin

/**
 * Created by ogulcan on 07/02/2018.
 */
class BaseApp : Application() {


    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()

        apolloClient = ApolloClient.builder().serverUrl(Constants.BASE_URL).build()
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);

        prefs = Prefs(applicationContext)
        startModules()
    }

    private fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    private fun startModules() {
        startKoin(this, listOf(
            appModule,
            controllersModule,
            viewModelsModule
        ))
    }

    companion object {
        lateinit var apolloClient: ApolloClient
        lateinit var instance: BaseApp private set
        lateinit var prefs: Prefs
    }

}