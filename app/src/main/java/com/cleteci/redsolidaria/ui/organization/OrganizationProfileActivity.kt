package com.cleteci.redsolidaria.ui.organization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.cleteci.redsolidaria.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_organization_profile.*
import kotlinx.android.synthetic.main.fragment_ranking.*
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
        start_5.setOnClickListener {
            start_5.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_4.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_2.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_1.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
        }
        start_4.setOnClickListener {
            start_5.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_4.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_2.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_1.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
        }
        start_3.setOnClickListener {
            start_5.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_4.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_2.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_1.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
        }
        start_2.setOnClickListener {
            start_5.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_4.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_2.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
            start_1.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
        }
        start_1.setOnClickListener {
            start_5.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_4.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_2.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_24))
            start_1.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_star_full_24))
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