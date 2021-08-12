package com.cleteci.redsolidaria.ui.organization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationById
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationsList
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.ui.base.withExtras
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_organization_profile.*
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

        with(intent.extras!!) {
            organizationId = getString(ORGANIZATION_ID, "")
            userId = getString(USER_ID, "")
        }

        if (organizationId.isBlank() && userId.isBlank()) return

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
        val adapter = SectionsPagerAdapter(this, supportFragmentManager)
        if (organization == null) {
            Toast.makeText(this,getString(R.string.error_organization_not_found), Toast.LENGTH_LONG).show()
            finish()
        } else {
            organization = getOrganizationsList()[0]// Using test data

            adapter.addFragment(InfoFragment(organization!!), "Info")
            adapter.addFragment(ServicesFragment(organization!!), "Services")
            adapter.addFragment(ActivitiesFragment(organization!!), "Activities")
            adapter.addFragment(ContributeFragment.newInstance(3), "Contribute")
            viewPager.adapter = adapter
            tabs.setupWithViewPager(viewPager)
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
            val tab = tabs.getTabAt(sectionId)
            Objects.requireNonNull<TabLayout.Tab>(tab).select()
        }

    }

    companion object {

        private const val ORGANIZATION_ID = "organization_id"
        private const val USER_ID = "user_id"

        fun newInstance(context: Context, organizationId: String = "", userId: String = ""): Intent {
            return Intent(context, OrganizationProfileActivity::class.java).withExtras(2) {
                putString(ORGANIZATION_ID, organizationId)
                putString(USER_ID, userId)
            }
        }
    }
}