package com.sugar.steptofood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_main_tabs.*
import kotlinx.android.synthetic.main.activity_main_tabs.view.*

class MainTabsActivity : AppCompatActivity() {

    private var sectionsPageAdapter: SectionsPageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_tabs)
        initTabs()

    }

    private fun initTabs() {
        this.sectionsPageAdapter = SectionsPageAdapter(this.supportFragmentManager)
        this.setupViewPager(pager)
        tabLayout.setupWithViewPager(pager)

        /*tabLayout?.addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == getString(R.string.tabRecipesText)) {
                    appBar?.elevation = 0f
                } else {
                    appBar?.elevation = 10f
                }
            }
        })*/
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = SectionsPageAdapter(this.supportFragmentManager)
        adapter.addFragment(RecipesFragment(), this.getString(R.string.tabRecipesText))
        adapter.addFragment(ComposeFragment(), this.getString(R.string.tabComposeText))
        adapter.addFragment(ComposeFragment(), this.getString(R.string.tabUserText))
        viewPager?.adapter = adapter
    }
}
