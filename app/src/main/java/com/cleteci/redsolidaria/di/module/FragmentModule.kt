package com.cleteci.redsolidaria.di.module


import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchContract
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchPresenter
import com.cleteci.redsolidaria.ui.fragments.basicsearch.BasicSearchContract
import com.cleteci.redsolidaria.ui.fragments.basicsearch.BasicSearchPresenter
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassContract
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassPresenter
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationContract
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationPresenter
import com.cleteci.redsolidaria.ui.fragments.contactUs.ContactUsContract
import com.cleteci.redsolidaria.ui.fragments.contactUs.ContactUsPresenter
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServiceContract
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServicePresenter
import com.cleteci.redsolidaria.ui.fragments.infoService.InfoServiceContract
import com.cleteci.redsolidaria.ui.fragments.infoService.InfoServicePresenter
import com.cleteci.redsolidaria.ui.fragments.login.LoginFContract
import com.cleteci.redsolidaria.ui.fragments.login.LoginFPresenter
import com.cleteci.redsolidaria.ui.fragments.map.MapContract
import com.cleteci.redsolidaria.ui.fragments.map.MapPresenter
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileContract
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfilePresenter
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderContract
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderPresenter
import com.cleteci.redsolidaria.ui.fragments.myResourses.MyResoursesContract
import com.cleteci.redsolidaria.ui.fragments.myResourses.MyResoursesPresenter
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultContract
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultPresenter
import com.cleteci.redsolidaria.ui.fragments.resoursesOffered.ResoursesOfferedContract
import com.cleteci.redsolidaria.ui.fragments.resoursesOffered.ResoursesOfferedPresenter
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodeContract
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodePresenter
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailContract
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailPresenter
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServiceContract
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServicePresenter
import com.cleteci.redsolidaria.ui.fragments.users.UsersContract
import com.cleteci.redsolidaria.ui.fragments.users.UsersPresenter
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomeContract
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomePresenter


import dagger.Module
import dagger.Provides

/**
 * Created by ogulcan on 07/02/2018.
 */
@Module
class FragmentModule {

    @Provides
    fun providePresenter(): AdvancedSearchContract.Presenter {
        return AdvancedSearchPresenter()
    }

    @Provides
    fun provideBasicSeachPresenter(): BasicSearchContract.Presenter {
        return BasicSearchPresenter()
    }

    @Provides
    fun provideResourseByCityPresenter(): ResourcesResultContract.Presenter {
        return ResourcesResultPresenter()
    }

    @Provides
    fun provideMyResoursesPresenterPresenter(): MyResoursesContract.Presenter {
        return MyResoursesPresenter()
    }

    @Provides
    fun provideMapPresenter(): MapContract.Presenter {
        return MapPresenter()
    }

    @Provides
    fun provideServiceDetailPresenter(): ServiceDetailContract.Presenter {
        return ServiceDetailPresenter()
    }

    @Provides
    fun provideSuggestServicePresenter(): SuggestServiceContract.Presenter {
        return SuggestServicePresenter()
    }

    @Provides
    fun provideConfigurationPresenter(): ConfigurationContract.Presenter {
        return ConfigurationPresenter()
    }


    @Provides
    fun provideContactUsPresenter(): ContactUsContract.Presenter {
        return ContactUsPresenter()
    }


    @Provides
    fun provideMyProfilePresenter(): MyProfileContract.Presenter {
        return MyProfilePresenter()
    }

    @Provides
    fun provideWelcomePresenter(): WelcomeContract.Presenter {
        return WelcomePresenter()
    }

    @Provides
    fun provideLoginFPresenter(): LoginFContract.Presenter {
        return LoginFPresenter()
    }

    @Provides
    fun provideResoursesOfferedPresenter(): ResoursesOfferedContract.Presenter {
        return ResoursesOfferedPresenter()
    }

    @Provides
    fun provideUsersPresenter(): UsersContract.Presenter {
        return UsersPresenter()
    }

    @Provides
    fun provideChangePassPresenter(): ChangePassContract.Presenter {
        return ChangePassPresenter()
    }

    @Provides
    fun provideScanCodePresenter(): ScanCodeContract.Presenter {
        return ScanCodePresenter()
    }

    @Provides
    fun provideMyProfileProviderPresenter(): MyProfileProviderContract.Presenter {
        return MyProfileProviderPresenter()
    }

    @Provides
    fun provideCreateServicePresenter(): CreateServiceContract.Presenter {
        return CreateServicePresenter()
    }

    @Provides
    fun provideInfoServicePresenter(): InfoServiceContract.Presenter {
        return InfoServicePresenter()
    }



}