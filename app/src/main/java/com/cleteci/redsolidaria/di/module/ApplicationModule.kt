package com.cleteci.redsolidaria.di.module

import android.app.Application
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ogulcan on 07/02/2018.
 */
@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}