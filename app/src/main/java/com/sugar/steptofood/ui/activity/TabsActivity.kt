package com.sugar.steptofood.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import com.sugar.steptofood.ui.fragment.compose.ComposeFragment
import com.sugar.steptofood.R
import com.sugar.steptofood.adapter.SectionsPageAdapter
import com.sugar.steptofood.ui.fragment.recipe.RecommendedRecipeFragment
import com.sugar.steptofood.ui.fragment.user.SessionUserFragment
import com.sugar.steptofood.ui.viewmodel.RecipeViewModel
import com.sugar.steptofood.utils.ExtraName.UID
import com.sugar.steptofood.utils.OPEN_GALLERY_PERMISSIONS_REQUEST_CODE
import com.sugar.steptofood.utils.hasStoragePermissions
import kotlinx.android.synthetic.main.activity_main_tabs.*

class TabsActivity : AppCompatActivity() {

    private val recipeViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }

    lateinit var sectionsPageAdapter: SectionsPageAdapter
        private set

    private var recipeFragment: RecommendedRecipeFragment? = null
    private var composeFragment: ComposeFragment? = null
    private var userFragment: SessionUserFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_tabs)
        initTabs()
    }

    private fun initTabs() {
        intent.putExtra(UID, recipeViewModel.session.userId)
        recipeFragment = RecommendedRecipeFragment.getInstance()
        composeFragment = ComposeFragment.getInstance()
        userFragment = SessionUserFragment.getInstance()

        setupViewPager(pager)
        tabLayout.setupWithViewPager(pager)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        sectionsPageAdapter = SectionsPageAdapter(this.supportFragmentManager)
        sectionsPageAdapter.addFragment(recipeFragment!!, this.getString(R.string.tabRecipesText))
        sectionsPageAdapter.addFragment(composeFragment!!, this.getString(R.string.tabComposeText))
        sectionsPageAdapter.addFragment(userFragment!!, this.getString(R.string.tabUserText))
        viewPager?.adapter = sectionsPageAdapter
    }

    override fun onBackPressed() {
        if (currentTabIsComposedRecipe()) {
            sectionsPageAdapter.replace(COMPOSE_TAB, composeFragment!!)
            pager.adapter?.notifyDataSetChanged()
        } else toHome()
    }

    private fun currentTabIsComposedRecipe(): Boolean = pager.currentItem == COMPOSE_TAB
            && sectionsPageAdapter.getItem(COMPOSE_TAB) !is ComposeFragment

    private fun toHome() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPEN_GALLERY_PERMISSIONS_REQUEST_CODE && hasStoragePermissions(this)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, SessionUserFragment.PICK_IMAGE)
        }
    }

    companion object {
        const val COMPOSE_TAB = 1
    }
}
