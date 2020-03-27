package com.cleteci.redsolidaria.ui.activities.main

import com.cleteci.redsolidaria.ui.base.BaseContract

/**
 * Created by ogulcan on 07/02/2018.
 */
class MainContract {

    interface View: BaseContract.View {
        fun init()
        fun showSearchFragment()
        fun showMapFragment()
        fun showResoursesFragment()

        fun showSuggestFragment()

        fun showConfigFragment()

        fun showProfileFragment()

        fun showContactFragment()

        fun showUsersFragment()

        fun showScanFragment()

        fun showResoursesProviderFragment()
    }

    interface Presenter: BaseContract.Presenter<MainContract.View> {

        fun onNavUsersOption()

        fun onNavScanOption()

        fun onNavResoursesProviderOption()


        fun onNavSearchOption()
        fun onNavMapOption()
        fun onNavResourcesOption()

        fun onDrawerSuggestOption()

        fun onDrawerConfigOption()

        fun onDrawerProfileOption()

        fun onDrawerContactOption()


    }
}