package com.cleteci.redsolidaria.di.modules

import com.cleteci.redsolidaria.network.GraphQLController
import com.cleteci.redsolidaria.network.GraphQLFactory
import com.cleteci.redsolidaria.ui.activities.main.MainContract
import com.cleteci.redsolidaria.ui.activities.main.MainPresenter
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordContract
import com.cleteci.redsolidaria.ui.activities.resetPassword.ResetPasswordPresenter
import com.cleteci.redsolidaria.ui.activities.splash.SplashContract
import com.cleteci.redsolidaria.ui.activities.splash.SplashPresenter
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchContract
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchPresenter
import com.cleteci.redsolidaria.ui.fragments.users.AttendersContract
import com.cleteci.redsolidaria.ui.fragments.users.AttendersPresenter
import com.cleteci.redsolidaria.ui.fragments.users.AttendersListContract
import com.cleteci.redsolidaria.ui.fragments.users.AttendersListPresenter
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassContract
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassPresenter
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationContract
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationPresenter
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServiceContract
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServicePresenter
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileContract
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfilePresenter
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderContract
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderPresenter
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodeContract
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodePresenter
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailContract
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailPresenter
import com.cleteci.redsolidaria.ui.fragments.users.UsersContract
import com.cleteci.redsolidaria.ui.fragments.users.UsersPresenter
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomeContract
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomePresenter
import com.cleteci.redsolidaria.ui.search.SearchContract
import com.cleteci.redsolidaria.ui.search.SearchPresenter
import com.cleteci.redsolidaria.viewModels.BeneficiaryViewModel
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


private const val GRAPH_QL_CLIENT = "graphQLClient"

val appModule = module {
    //NetworkClients
    single(named(GRAPH_QL_CLIENT)) { GraphQLFactory().graphQLAppServicesInstance }
}

val controllersModule = module {
    single { GraphQLController(get(named(GRAPH_QL_CLIENT))) }
}

val presentersModule = module {
    factory<MainContract.Presenter> { MainPresenter() }
    factory<SplashContract.Presenter> { SplashPresenter() }
    factory<ResetPasswordContract.Presenter> { ResetPasswordPresenter() }
    factory<AdvancedSearchContract.Presenter> { AdvancedSearchPresenter() }
    factory<SearchContract.Presenter> { SearchPresenter() }
    factory<ServiceDetailContract.Presenter> { ServiceDetailPresenter() }
    factory<ConfigurationContract.Presenter> { ConfigurationPresenter() }
    factory<MyProfileContract.Presenter> { MyProfilePresenter() }
    factory<WelcomeContract.Presenter> { WelcomePresenter() }
    factory<UsersContract.Presenter> { UsersPresenter() }
    factory<ChangePassContract.Presenter> { ChangePassPresenter() }
    factory<ScanCodeContract.Presenter> { ScanCodePresenter() }
    factory<MyProfileProviderContract.Presenter> { MyProfileProviderPresenter() }
    factory<CreateServiceContract.Presenter> { CreateServicePresenter() }
    factory<AttendersContract.Presenter> { AttendersPresenter() }
    factory<AttendersListContract.Presenter> { AttendersListPresenter() }
}

//Don't forget to add the constructor dependencies to viewModels (Crash will occur on release build)
val viewModelsModule = module {
    viewModel { BeneficiaryViewModel(get()) }
    viewModel { GeneralViewModel(get()) }
    viewModel { OrganizationViewModel(get()) }
    viewModel { UserAccountViewModel(get()) }

}