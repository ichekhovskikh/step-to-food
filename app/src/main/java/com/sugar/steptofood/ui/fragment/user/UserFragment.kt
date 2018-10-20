package com.sugar.steptofood.ui.fragment.user

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import android.widget.TextView
import com.sugar.steptofood.ui.FoodView
import com.sugar.steptofood.ui.activity.AddFoodActivity
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.activity.UserItemActivity
import com.sugar.steptofood.ui.factory.DialogFactory
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import kotlinx.android.synthetic.main.fragment_user.*

open class UserFragment : FoodView, BaseFragment() {

    companion object {
        fun getInstance() = UserFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initMenuItems(view)
        initUserImage(view)

        /*TODO set user name*/
        setUserName(view, "Иван Чеховских")
    }

    override fun getLayout(): Int = R.layout.fragment_user

    open fun initMenuItems(view: View) {
        initAddFood(itemMenuContainer)
        initAddedRecipes(itemMenuContainer)
        initLikeRecipes(itemMenuContainer)
        initExit(itemMenuContainer)
    }

    private fun initUserImage(view: View) {
        /*TODO set user img*/
    }

    private fun setUserName(view: View, userName: String) {
        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = userName
    }

    fun initAddFood(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.add_black,
                R.string.add_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, AddFoodActivity::class.java)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initAddedRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.menu_black,
                R.string.added_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, UserItemActivity::class.java)
            intent.putExtra(ITEM_TYPE, UserItemActivity.ItemType.ADDED)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initLikeRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.like_black,
                R.string.like_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, UserItemActivity::class.java)
            intent.putExtra(ITEM_TYPE, UserItemActivity.ItemType.LIKE)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initExit(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.arrow_back_black,
                R.string.exit_menu_item)

        button.setOnClickListener {
            DialogFactory.createExitDialog(context) { exit() }.show()
        }
        container.addView(button)
    }

    fun setUserAvatar(image: Bitmap) {

    }

    private fun createButton(resource: Int, @Nullable imgResource: Int, textResource: Int): Button {
        val button: Button = inflater?.inflate(resource, null) as Button
        button.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        button.text = getString(textResource)
        return button
    }

    private fun exit() {
        val intent = Intent(activity, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
