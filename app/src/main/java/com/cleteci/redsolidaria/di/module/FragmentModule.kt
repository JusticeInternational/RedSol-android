package com.cleteci.redsolidaria.di.module


import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchContract
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchPresenter
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
import com.cleteci.redsolidaria.ui.fragments.infoService.ScanNoUserContract
import com.cleteci.redsolidaria.ui.fragments.infoService.ScanNoUserPresenter
import com.cleteci.redsolidaria.ui.fragments.login.LoginFContract
import com.cleteci.redsolidaria.ui.fragments.login.LoginFPresenter
import com.cleteci.redsolidaria.ui.search.SearchContract
import com.cleteci.redsolidaria.ui.search.SearchPresenter
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileContract
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfilePresenter
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderContract
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderPresenter
import com.cleteci.redsolidaria.ui.fragments.myResources.MyResourcesContract
import com.cleteci.redsolidaria.ui.fragments.myResources.MyResourcesPresenter
import com.cleteci.redsolidaria.ui.fragments.resourcesOffered.ResourcesOfferedContract
import com.cleteci.redsolidaria.ui.fragments.resourcesOffered.ResourcesOfferedPresenter
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodeContract
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodePresenter
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailContract
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailPresenter
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServiceContract
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServicePresenter
import com.cleteci.redsolidaria.ui.fragments.users.*
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
    fun provideMyResoursesPresenterPresenter(): MyResourcesContract.Presenter {
        return MyResourcesPresenter()
    }

    @Provides
    fun provideMapPresenter(): SearchContract.Presenter {
        return SearchPresenter()
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
    fun provideResoursesOfferedPresenter(): ResourcesOfferedContract.Presenter {
        return ResourcesOfferedPresenter()
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

    @Provides
    fun provideAttendersPresenter(): AttendersContract.Presenter {
        return AttendersPresenter()
    }

    @Provides
    fun provideAttendersListContractPresenter(): AttendersListContract.Presenter {
        return AttendersListPresenter()
    }

    @Provides
    fun provideScanNoUserContractPresenter(): ScanNoUserContract.Presenter {
        return ScanNoUserPresenter()
    }




}