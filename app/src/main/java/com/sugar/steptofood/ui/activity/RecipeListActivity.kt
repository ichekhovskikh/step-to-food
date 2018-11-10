package com.sugar.steptofood.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.fragment.recipe.LikeRecipeFragment
import com.sugar.steptofood.ui.fragment.recipe.AddedRecipeFragment
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import com.sugar.steptofood.utils.RecipeType

class RecipeListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_recipes)
        val type = intent.getSerializableExtra(ITEM_TYPE) as RecipeType
        selectFragment(type)
    }

    private fun selectFragment(type: RecipeType) {
        when (type) {
            RecipeType.ADDED -> setFragment(AddedRecipeFragment.getInstance())
            RecipeType.LIKE -> setFragment(LikeRecipeFragment.getInstance())
            else -> {
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
    }
}