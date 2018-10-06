package com.sugar.steptofood

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import kotterknife.bindView;


class TabsMainActivity : AppCompatActivity() {
    val viewPager: ViewPager? by bindView(R.id.pager)
    val tabLayout: TabLayout? by bindView(R.id.tabLayout)
//    val appBar: AppBarLayout? by bindView(R.id.appbar)

    private var sectionsPageAdapter: SectionsPageAdapter? = null

/*    @BindView(R.id.search)
    private var searchView: SearchView? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getSupportActionBar()?.hide()
        this.setContentView(R.layout.activity_tabs_main)
        initializeTabs()

    }

    private fun initializeTabs() {
        this.sectionsPageAdapter = SectionsPageAdapter(this.getSupportFragmentManager())
        this.setupViewPager(viewPager)
        tabLayout?.setupWithViewPager(viewPager)

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
