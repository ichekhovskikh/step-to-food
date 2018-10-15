package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.sugar.steptofood.ui.fragment.compose.ComposeFragment
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.recipes.RecipesFragment
import com.sugar.steptofood.ui.fragment.user.UserFragment
import com.sugar.steptofood.adapter.SectionsPageAdapter
import kotlinx.android.synthetic.main.activity_main_tabs.*

class TabsActivity : AppCompatActivity() {

    companion object {
        val RECIPES_TAB = 0
        val COMPOSE_TAB = 1
        val USER_TAB = 2
    }

    var sectionsPageAdapter: SectionsPageAdapter? = null

    private val recipesFragment: RecipesFragment = RecipesFragment.getInstance()
    private val composeFragment: ComposeFragment = ComposeFragment.getInstance()
    private val userFragment: UserFragment = UserFragment.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_tabs)
        initTabs()

    }

    private fun initTabs() {
        this.setupViewPager(pager)
        tabLayout.setupWithViewPager(pager)
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
        } else toHome()

    }

    private fun currentTabIsComposedFoods(): Boolean = pager.currentItem == COMPOSE_TAB
            && sectionsPageAdapter?.getItem(COMPOSE_TAB) !is ComposeFragment

    private fun toHome() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}
