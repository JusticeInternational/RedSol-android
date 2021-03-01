package com.cleteci.redsolidaria.ui.organization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleteci.redsolidaria.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_organization_profile.*
import java.util.*

class OrganizationProfileActivity : AppCompatActivity() {
    private var sectionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_profile)
        setUpUI()
    }

    private fun setUpUI() {
        appBarTitle.text = ""
        setupTabs()
    }

    private fun setupTabs() {
        val adapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        adapter.addFragment(InfoFragment.newInstance(0), "Info")
        adapter.addFragment(InfoFragment.newInstance(1), "Info1")
        adapter.addFragment(InfoFragment.newInstance(1), "Info3")
        adapter.addFragment(ContributeFragment.newInstance(1), "Contribute")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        val tab = tabs.getTabAt(sectionId)
        Objects.requireNonNull<TabLayout.Tab>(tab).select()
    }
}