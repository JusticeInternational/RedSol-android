package com.cleteci.redsolidaria.ui.activities.main

import com.cleteci.redsolidaria.ui.base.BaseContract

class MainContract {

    interface View: BaseContract.View {
        fun init()

        fun showHomeFragment()

        fun showSearchWithMapFragment()

        fun showMapFragment()

        fun showResourcesFragment()

        fun showSuggestFragment()

        fun showConfigFragment()

        fun showProfileFragment()

        fun showContactFragment()

        fun showUsersFragment()

        fun showScanFragment(serviceID: String?, catId: String?, name: String?, isGeneric: Boolean)

        fun showScanListFragment()

        fun showResourcesProviderFragment()
    }

    interface Presenter: BaseContract.Presenter<View> {

        fun onDrawerHomeOption()

        fun onNavUsersOption()

        fun onNavScanOption()

        fun onNavResourcesProviderOption()

        fun onNavSearchOption()

        fun onNavMapOption()

        fun onNavResourcesOption()

        fun onDrawerSuggestOption()

        fun onDrawerConfigOption()

        fun onDrawerProfileOption()

        fun onDrawerContactOption()


    }
}