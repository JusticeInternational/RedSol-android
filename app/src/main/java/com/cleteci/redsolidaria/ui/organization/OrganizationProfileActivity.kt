package com.cleteci.redsolidaria.ui.organization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.cleteci.redsolidaria.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_organization_profile.*
import java.util.*

class OrganizationProfileActivity : AppCompatActivity() {
    private var sectionId: Int = 0
    private var isoOrganizationSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_profile)
        setUpUI()
    }

    private fun setUpUI() {
        setupTabs()
        heartIcon.setOnClickListener {
            heartIcon.setImageDrawable(AppCompatResources.getDrawable(this,
                if (isoOrganizationSaved) R.drawable.ic_heart_saved else R.drawable.ic_heart_unsaved))
            isoOrganizationSaved = !isoOrganizationSaved
        }
    }

    private fun setupTabs() {
        val adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        adapter.addFragment(InfoFragment.newInstance(0), "Info")
        adapter.addFragment(ServicesFragment.newInstance(1), "Services")
        adapter.addFragment(ActivitiesFragment.newInstance(1), "Activities")
        adapter.addFragment(ContributeFragment.newInstance(1), "Contribute")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        val tab = tabs.getTabAt(sectionId)
        Objects.requireNonNull<TabLayout.Tab>(tab).select()
    }
}