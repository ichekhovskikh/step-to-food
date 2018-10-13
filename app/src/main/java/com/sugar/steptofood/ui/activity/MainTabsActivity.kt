package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.sugar.steptofood.ui.fragment.ComposeFragment
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.RecipesFragment
import com.sugar.steptofood.ui.fragment.UserFragment
import com.sugar.steptofood.adapter.SectionsPageAdapter
import kotlinx.android.synthetic.main.activity_main_tabs.*

class MainTabsActivity : AppCompatActivity() {

    companion object {
        val RECIPES_TAB = 0
        val COMPOSE_TAB = 1
        val USER_TAB = 2
    }

    var sectionsPageAdapter: SectionsPageAdapter? = null

    val recipesFragment: RecipesFragment = RecipesFragment.getInstance()
    val composeFragment: ComposeFragment = ComposeFragment.getInstance()
    val userFragment: UserFragment = UserFragment.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_tabs)
        initTabs()

    }

    private fun initTabs() {
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
        sectionsPageAdapter = SectionsPageAdapter(this.supportFragmentManager)
        sectionsPageAdapter?.addFragment(recipesFragment, this.getString(R.string.tabRecipesText))
        sectionsPageAdapter?.addFragment(composeFragment, this.getString(R.string.tabComposeText))
        sectionsPageAdapter?.addFragment(userFragment, this.getString(R.string.tabUserText))
        viewPager?.adapter = sectionsPageAdapter
    }

    override fun onBackPressed() {
        if (currentTabIsComposedFoods()) {
            sectionsPageAdapter?.replace(COMPOSE_TAB, composeFragment)
            pager.adapter?.notifyDataSetChanged()
        }
        else toHome()

    }

    private fun currentTabIsComposedFoods(): Boolean = pager.currentItem == COMPOSE_TAB
            && sectionsPageAdapter?.getItem(COMPOSE_TAB) !is ComposeFragment

    private fun toHome() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
