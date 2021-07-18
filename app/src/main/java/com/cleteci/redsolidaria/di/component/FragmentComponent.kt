package com.cleteci.redsolidaria.di.component

import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.cleteci.redsolidaria.ui.fragments.basicsearch.BasicSearchFragment
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassFragment
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationFragment
import com.cleteci.redsolidaria.ui.fragments.contactUs.ContactUsFragment
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServiceFragment
import com.cleteci.redsolidaria.ui.fragments.infoService.InfoServiceFragment
import com.cleteci.redsolidaria.ui.fragments.infoService.ScanNoUserFragment
import com.cleteci.redsolidaria.ui.fragments.login.LoginFFragment
import com.cleteci.redsolidaria.ui.search.SearchFragment
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileFragment
import com.cleteci.redsolidaria.ui.fragments.myProfileProvider.MyProfileProviderFragment
import com.cleteci.redsolidaria.ui.fragments.myResources.MyResourcesFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesOffered.ResourcesOfferedFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultFragment
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodeFragment
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailFragment
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServiceFragment
import com.cleteci.redsolidaria.ui.fragments.users.AttendersFragment
import com.cleteci.redsolidaria.ui.fragments.users.UsersFragment
import com.cleteci.redsolidaria.ui.fragments.users.AttendersListFragment
import com.cleteci.redsolidaria.ui.fragments.welcome.WelcomeFragment
import dagger.Component


@Component(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(basicSearchFragment: BasicSearchFragment)

    fun inject(advancedSearchFragment: AdvancedSearchFragment)

    fun inject(serviceDetailFragment: ServiceDetailFragment)

    fun inject(resourcesResultFragment: ResourcesResultFragment)

    fun inject(myResourcesFragment: MyResourcesFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(suggestServiceFragment: SuggestServiceFragment)

    fun inject(configurationFragment: ConfigurationFragment)

    fun inject(contactUsFragment: ContactUsFragment)

    fun inject(myProfileFragment: MyProfileFragment)

    fun inject(welcomeFragment: WelcomeFragment)

    fun inject(loginFFragment: LoginFFragment)

    fun inject(usersFragment: UsersFragment)

    fun inject(scanCodeFragment: ScanCodeFragment)

    fun inject(resourcesOfferedFragment: ResourcesOfferedFragment)

    fun inject(changePassFragment: ChangePassFragment)

    fun inject(myProfileProviderFragment: MyProfileProviderFragment)

    fun inject(createServiceFragment: CreateServiceFragment)

    fun inject(infoServiceFragment: InfoServiceFragment)

    fun inject(attendersFragment: AttendersFragment)

    fun inject(attendersListFragment: AttendersListFragment)

    fun inject(scanNoUserFragment: ScanNoUserFragment)



}