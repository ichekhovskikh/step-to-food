package com.sugar.steptofood.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import com.sugar.steptofood.App
import com.sugar.steptofood.ui.fragment.compose.ComposeFragment
import com.sugar.steptofood.R
import com.sugar.steptofood.Session
import com.sugar.steptofood.ui.fragment.recipes.RecipesFragment
import com.sugar.steptofood.ui.fragment.user.UserFragment
import com.sugar.steptofood.adapter.SectionsPageAdapter
import com.sugar.steptofood.utils.ExtraName.UID
import com.sugar.steptofood.utils.OPEN_GALLERY_PERMISSIONS_REQUEST_CODE
import com.sugar.steptofood.utils.hasStoragePermissions
import kotlinx.android.synthetic.main.activity_main_tabs.*
import javax.inject.Inject

class TabsActivity : AppCompatActivity() {

    @Inject
    lateinit var session: Session

    lateinit var sectionsPageAdapter: SectionsPageAdapter
        private set

    private var recipesFragment: RecipesFragment? = null
    private var composeFragment: ComposeFragment? = null
    private var userFragment: UserFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        this.setContentView(R.layout.activity_main_tabs)
        initTabs()
    }

    private fun initTabs() {
        intent.putExtra(UID, session.userId)
        recipesFragment = RecipesFragment.getInstance()
        composeFragment = ComposeFragment.getInstance()
        userFragment = UserFragment.getInstance()

        setupViewPager(pager)
        tabLayout.setupWithViewPager(pager)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        sectionsPageAdapter = SectionsPageAdapter(this.supportFragmentManager)
        sectionsPageAdapter.addFragment(recipesFragment!!, this.getString(R.string.tabRecipesText))
        sectionsPageAdapter.addFragment(composeFragment!!, this.getString(R.string.tabComposeText))
        sectionsPageAdapter.addFragment(userFragment!!, this.getString(R.string.tabUserText))
        viewPager?.adapter = sectionsPageAdapter
    }

    override fun onBackPressed() {
        if (currentTabIsComposedFood()) {
            sectionsPageAdapter.replace(COMPOSE_TAB, composeFragment!!)
            pager.adapter?.notifyDataSetChanged()
        } else toHome()

    }

    private fun currentTabIsComposedFood(): Boolean = pager.currentItem == COMPOSE_TAB
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
            startActivityForResult(intent, UserFragment.PICK_IMAGE)
        }
    }

    companion object {
        const val RECIPES_TAB = 0
        const val COMPOSE_TAB = 1
        const val USER_TAB = 2
    }
}
