package com.cleteci.redsolidaria.di.component

import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.di.module.ApplicationModule
import dagger.Component

/**
 * Created by ogulcan on 07/02/2018.
 */
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: BaseApp)

}