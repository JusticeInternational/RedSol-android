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
import com.cleteci.redsolidaria.ui.search.SearchContract
import com.cleteci.redsolidaria.ui.search.SearchPresenter
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileContract
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfilePresenter
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderContract
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderPresenter
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
    fun provideAttendersPresenter(): AttendersContract.Presenter {
        return AttendersPresenter()
    }

    @Provides
    fun provideAttendersListContractPresenter(): AttendersListContract.Presenter {
        return AttendersListPresenter()
    }
}