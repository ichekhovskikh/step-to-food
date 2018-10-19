package com.sugar.steptofood.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sugar.steptofood.App
import com.sugar.steptofood.R
import com.sugar.steptofood.db.SQLiteHelper
import com.sugar.steptofood.ui.fragment.recipes.UserLikeFragment
import com.sugar.steptofood.ui.fragment.recipes.UserRecipesFragment
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import javax.inject.Inject

class UserItemActivity : AppCompatActivity() {

    @Inject
    lateinit var dbHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        setContentView(R.layout.activity_user_item)
        //TODO extra user
        val type = intent.getSerializableExtra(ITEM_TYPE) as ItemType
        selectFragment(type)
    }

    private fun selectFragment(type: ItemType) {
        when (type) {
            ItemType.ADDED -> setFragment(UserRecipesFragment.getInstance())
            ItemType.LIKE -> setFragment(UserLikeFragment.getInstance())
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }

    enum class ItemType {
        LIKE,
        ADDED
    }
}