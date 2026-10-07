package com.cleteci.redsolidaria

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient

import com.cleteci.redsolidaria.di.modules.appModule
import com.cleteci.redsolidaria.di.modules.controllersModule
import com.cleteci.redsolidaria.di.modules.presentersModule
import com.cleteci.redsolidaria.di.modules.viewModelsModule
import com.cleteci.redsolidaria.util.Constants
import com.cleteci.redsolidaria.util.SharedPreferences
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        apolloClient = ApolloClient.builder().serverUrl(Constants.BASE_URL).build()
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);

        sharedPreferences = SharedPreferences(applicationContext)
        startModules()
    }

    private fun startModules() {
        startKoin {
            androidContext(this@BaseApp)
            modules(
                listOf(
                    appModule,
                    controllersModule,
                    presentersModule,
                    viewModelsModule
                )
            )
        }
    }

    companion object {
        lateinit var apolloClient: ApolloClient
        lateinit var instance: BaseApp private set
        lateinit var sharedPreferences: SharedPreferences

        @JvmStatic
        fun getContext(): Context = instance.applicationContext
    }

}