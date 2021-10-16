package com.cleteci.redsolidaria.ui.activities.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerActivityComponent
import com.cleteci.redsolidaria.di.module.ActivityModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.ui.login.LoginActivity
import com.cleteci.redsolidaria.ui.activities.splash.SplashActivity
import com.cleteci.redsolidaria.ui.customUIComponents.FragNavController
import com.cleteci.redsolidaria.ui.customUIComponents.FragmentHistory
import com.cleteci.redsolidaria.ui.fragments.basicsearch.BasicSearchFragment
import com.cleteci.redsolidaria.ui.fragments.changePassword.ChangePassFragment
import com.cleteci.redsolidaria.ui.fragments.configuration.ConfigurationFragment
import com.cleteci.redsolidaria.ui.fragments.contactUs.ContactUsFragment
import com.cleteci.redsolidaria.ui.fragments.createService.CreateServiceFragment
import com.cleteci.redsolidaria.ui.fragments.infoService.InfoServiceFragment
import com.cleteci.redsolidaria.ui.fragments.myProfile.MyProfileFragment
import com.cleteci.redsolidaria.ui.resources.BeneficiaryResourcesFragment
import com.cleteci.redsolidaria.ui.resources.ResourcesOfferedFragment
import com.cleteci.redsolidaria.ui.fragments.scanCode.ScanCodeFragment
import com.cleteci.redsolidaria.ui.fragments.scanNoUser.ScanNoUserFragment
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServiceFragment
import com.cleteci.redsolidaria.ui.fragments.users.AttendersFragment
import com.cleteci.redsolidaria.ui.fragments.users.UsersFragment
import com.cleteci.redsolidaria.ui.organizationProfile.OrganizationProfileActivity
import com.cleteci.redsolidaria.ui.search.SearchFragment
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.SEARCH_REQUEST_CODE
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainContract.View,
    NavigationView.OnNavigationItemSelectedListener,
    FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    @Inject
    lateinit var presenter: MainContract.Presenter
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private var mNavController: FragNavController? = null
    private var fragmentHistory: FragmentHistory? = null
    private var currentFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BaseApp.sharedPreferences.isFirstTime) {
            BaseApp.sharedPreferences.isFirstTime = false
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else if (BaseApp.sharedPreferences.userSaved != null || BaseApp.sharedPreferences.loginLater) {
            setContentView(R.layout.activity_main)
            injectDependency()
            fragmentHistory = FragmentHistory()
            mNavController = FragNavController.newBuilder(
                savedInstanceState,
                supportFragmentManager,
                R.id.container_fragment
            ).transactionListener(this)
                .rootFragmentListener(this, TABS.size)
                .build()
            presenter.attach(this)
        } else {
            goToLogin()
            finish()
        }
        configureGoogleSignIn()
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.inject(this)
    }

    override fun init() {
        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        lyLoginLogout!!.setOnClickListener {
            if (!BaseApp.sharedPreferences.loginLater) {
                signOut()
                goToLogin()
                finish()
            } else {
                goToLogin()
            }
        }
        mapListButton.setOnClickListener {
            if (currentFragment != null && currentFragment is SearchFragment) {
                (currentFragment as SearchFragment).onClickMapListButton(it as ImageView)
            }
        }
        searchButton.setOnClickListener {
            presenter.onNavSearchOption()
        }

        if (BaseApp.sharedPreferences.loginLater) {
            tvLoginLogout!!.setText(R.string.login)
            icLoginLogout.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_login_24
                )
            )
        } else {
            tvLoginLogout!!.setText(R.string.logout)
            icLoginLogout.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_logout_24
                )
            )
        }

        bottomNavView.menu.clear(); //clear old inflated items.
        if (BaseApp.sharedPreferences.isProviderService) {
            bottomNavView.inflateMenu(R.menu.bottom_nav_menu_provider)
        } else {
            bottomNavView.inflateMenu(R.menu.bottom_nav_menu)
        }

        val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        presenter.onNavResourcesOption()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_search_map_list -> {
                        presenter.onNavMapOption()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_search -> {
                        showSearchFragment()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_home_provider -> {
                        presenter.onNavResourcesProviderOption()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_scan_provider -> {
                        presenter.onNavScanOption()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_users_providers -> {
                        presenter.onNavUsersOption()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        switchTab(0)
    }

    override fun showHomeFragment() {
        if (BaseApp.sharedPreferences.isProviderService) {
            presenter.onNavResourcesProviderOption()
        } else {
            presenter.onNavResourcesOption()
        }
    }

    fun setSearchLabel(newLabel: String) {
        searchLabel.text = newLabel
    }

    fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun switchTab(position: Int) {
        mNavController!!.switchTab(position)
    }

    override fun showUsersFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, UsersFragment().newInstance(), UsersFragment.TAG)
            .commit()
    }

    override fun showScanFragment(
        serviceID: String?,
        catId: String?,
        name: String?,
        isGeneric: Boolean
    ) {
        alertConfirmation(isGeneric, name, serviceID, catId)
    }

    private fun alertConfirmation(
        isGeneric: Boolean,
        name: String?,
        serviceID: String?,
        catID: String?
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_scan)

        val yesBtn = dialog.findViewById(R.id.btCont) as Button
        val btCancel = dialog.findViewById(R.id.btCancel) as Button

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

        val tvAlertMsg = dialog.findViewById(R.id.tvAlertMsg) as TextView

        if (isGeneric) {
            tvAlertMsg.text = String.format(
                getString(R.string.count_question_1),
                name
            )
        } else {
            tvAlertMsg.text = String.format(
                getString(R.string.count_question_2),
                name
            )
        }

        yesBtn.setOnClickListener {
            dialog.dismiss()

            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(
                    R.id.container_fragment,
                    ScanCodeFragment().newInstance(serviceID, catID, name, isGeneric),
                    ScanCodeFragment.TAG
                )
                .commit()
        }
        dialog.show()
    }

    override fun showScanListFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ResourcesOfferedFragment.newInstance(true),
                ResourcesOfferedFragment.TAG
            )
            .commit()
    }

    override fun showResourcesProviderFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ResourcesOfferedFragment.newInstance(false),
                ResourcesOfferedFragment.TAG
            )
            .commit()

    }

    override fun showSearchWithMapFragment() {
        val intent = Intent(this, SearchItemsActivity::class.java)
        if (currentFragment != null && currentFragment is SearchFragment) {
            currentFragment!!.startActivityForResult(intent, SEARCH_REQUEST_CODE)
        }
    }

    private fun showSearchFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, BasicSearchFragment(), BasicSearchFragment.TAG)
            .commit()
    }

    fun openOrganizationProfile(organizationId: String) {
        startActivity(
            OrganizationProfileActivity.newInstance(
                this,
                organizationId = organizationId
            )
        )
    }

    private fun openOrganizationProfileByUserId(userId: String) {
        startActivity(OrganizationProfileActivity.newInstance(this, userId = userId))
    }

    fun openCreateServiceFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                CreateServiceFragment().newInstance(),
                CreateServiceFragment.TAG
            )
            .commit()
    }

    fun openScanNoUserFragment(
        serviceID: String?,
        catId: String?,
        name: String?,
        isGeneric: Boolean
    ) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ScanNoUserFragment.newInstance(serviceID, catId, name, isGeneric),
                ScanNoUserFragment.TAG
            )
            .commit()
    }

    fun openAttendFragment(resourceId: String, resourceType: Resource.Type) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                AttendersFragment().newInstance(resourceId, resourceType),
                CreateServiceFragment.TAG
            )
            .commit()
    }

    fun openInfoFragment(resource: Resource) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                InfoServiceFragment.newInstance(resource),
                InfoServiceFragment.TAG
            )
            .commit()
    }

    fun showChangePassFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, ChangePassFragment(), ChangePassFragment.TAG)
            .commit()
    }

    private fun openSuggestFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                SuggestServiceFragment().newInstance(),
                SuggestServiceFragment.TAG
            )
            .commit()
    }

    private fun openProfileFragment() {
        if (BaseApp.sharedPreferences.loginLater) {
            showDialog()
        } else {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container_fragment, MyProfileFragment(), MyProfileFragment.TAG)
                .commit()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_go_to_login)

        val yesBtn = dialog.findViewById(R.id.btLogin) as Button

        val btCancel = dialog.findViewById(R.id.btCancel) as Button

        yesBtn.setOnClickListener {
            goToLogin()
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openConfigFragment() {
        if (BaseApp.sharedPreferences.loginLater) {
            showDialog()
        } else {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(
                    R.id.container_fragment,
                    ConfigurationFragment().newInstance(),
                    ConfigurationFragment.TAG
                )
                .commit()
        }
    }

    private fun openContactUsFragment() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ContactUsFragment().newInstance(),
                ContactUsFragment.TAG
            )
            .commit()
    }

    override fun showMapFragment() {
        searchButton.visibility = View.VISIBLE
        appBarTitle.visibility = View.GONE
        if (currentFragment == null || currentFragment !is SearchFragment) {
            currentFragment = SearchFragment()
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, currentFragment as SearchFragment, SearchFragment.TAG)
            .commit()
    }

    override fun showResourcesFragment() {
        searchButton.visibility = View.GONE
        appBarTitle.visibility = View.VISIBLE
        appBarTitle.text = getString(R.string.my_resources)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container_fragment, BeneficiaryResourcesFragment(), BeneficiaryResourcesFragment.TAG)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun showSuggestFragment() {
        openSuggestFragment()
    }

    override fun showConfigFragment() {
        openConfigFragment()
    }

    override fun showProfileFragment() {
        if (BaseApp.sharedPreferences.isProviderService) {
            bottomNavView.visibility = View.VISIBLE
            val organizationId = BaseApp.sharedPreferences.currentOrganizationId
            if (organizationId.isNullOrEmpty()) {
                val toast = Toast.makeText(
                    this,
                    getString(R.string.error_organization_not_found),
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } else {
                openOrganizationProfile(organizationId)
            }
        } else {
            bottomNavView.visibility = View.GONE
            openProfileFragment()
        }
    }

    override fun showContactFragment() {
        openContactUsFragment()
    }

    fun setTextToolbar(text: String, color: Int) {
        searchButton.visibility = View.GONE
        appBarTitle.visibility = View.VISIBLE

        toolbar.title = ""
        appBarTitle.text = text
        toolbar.setBackgroundColor(color)
    }

    fun setSearchButton() {
        searchButton.visibility = View.VISIBLE
        appBarTitle.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getRootFragment(index: Int): Fragment {
        if (!BaseApp.sharedPreferences.isProviderService) {
            when (index) {

                FragNavController.TAB1 -> {
                    if (currentFragment == null || currentFragment !is BeneficiaryResourcesFragment) {
                        currentFragment = BeneficiaryResourcesFragment()
                    }
                    return currentFragment as BeneficiaryResourcesFragment
                }
                FragNavController.TAB2 -> {
                    if (currentFragment == null || currentFragment !is SearchFragment) {
                        currentFragment = SearchFragment()
                    }
                    return currentFragment as Fragment
                }
                FragNavController.TAB3 -> return BasicSearchFragment()

            }
            throw IllegalStateException("Need to send an index that we know")
        } else {
            when (index) {

                FragNavController.TAB1 -> return ResourcesOfferedFragment()
                FragNavController.TAB2 -> return ResourcesOfferedFragment()
                FragNavController.TAB3 -> return UsersFragment()

            }
            throw IllegalStateException("Need to send an index that we know")
        }
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        if (supportActionBar != null && mNavController != null) {
        }
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType?
    ) {
        if (supportActionBar != null && mNavController != null) {
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                presenter.onDrawerHomeOption()
                bottomNavView.visibility = View.VISIBLE
            }
            R.id.nav_profile -> {
                presenter.onDrawerProfileOption()
            }
            R.id.nav_gallery -> {
                presenter.onDrawerConfigOption()
                bottomNavView.visibility = View.GONE
            }
            R.id.nav_slideshow -> {
                presenter.onDrawerContactOption()
                bottomNavView.visibility = View.GONE
            }
            R.id.nav_tools -> {
                presenter.onDrawerSuggestOption()
                bottomNavView.visibility = View.GONE
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        mGoogleSignInClient.signOut()
        BaseApp.sharedPreferences.logout()

    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    companion object {
        val TABS = arrayOf("MyResourses", "Map", "Search")
    }

}