package com.cleteci.redsolidaria.ui.organization

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationById
import com.cleteci.redsolidaria.models.Organization
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_organization_profile.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import java.util.*

class OrganizationProfileActivity : AppCompatActivity() {
    private var sectionId: Int = 0
    private var isoOrganizationSaved = false
    private var organizationId: String = ""
    private var userId: String = ""
    private var organization: Organization? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_profile)

        val messageBundle = intent.extras
        if (messageBundle != null) {
            organizationId = messageBundle.getString(ORGANIZATION_ID, "")
            userId = messageBundle.getString(USER_ID, "")
        }

        if (organizationId.isBlank() && userId.isBlank())
            return

        organization = getOrganizationById(if (userId.isBlank()) organizationId else userId)
        setUpUI()
    }

    private fun setUpUI() {
        setupTabs()

        if (organization != null) {
            name.text = organization!!.name
        }

        if (BaseApp.prefs.is_provider_service) {
            heartIcon.visibility = View.GONE
        } else {
            heartIcon.visibility = View.VISIBLE
            heartIcon.setOnClickListener {
                heartIcon.setImageDrawable(AppCompatResources.getDrawable(this,
                    if (isoOrganizationSaved) R.drawable.ic_heart_saved else R.drawable.ic_heart_unsaved))
                isoOrganizationSaved = !isoOrganizationSaved
            }
        }
    }

    private fun setupTabs() {
        val adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        adapter.addFragment(InfoFragment(organization), "Info")
        adapter.addFragment(ServicesFragment(organization), "Services")
        adapter.addFragment(ActivitiesFragment(organization), "Activities")
        adapter.addFragment(ContributeFragment.newInstance(3), "Contribute")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        val tab = tabs.getTabAt(sectionId)
        Objects.requireNonNull<TabLayout.Tab>(tab).select()
    }
     companion object {
         const val ORGANIZATION_ID = "organization_id"
         const val USER_ID = "user_id"
     }
}