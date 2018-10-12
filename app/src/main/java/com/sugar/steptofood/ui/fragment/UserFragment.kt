package com.sugar.steptofood.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import android.widget.TextView
import com.sugar.steptofood.ui.activity.LoginActivity


class UserFragment : BaseFragment() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initMenuItems(view)
        initUserImage(view)

        /*TODO set user name*/
        setUserName(view, "Иван Чеховских")
    }

    override fun getLayout(): Int = R.layout.fragment_user

    private fun initMenuItems(view: View) {
        val menuContainer = view.findViewById<LinearLayout>(R.id.itemMenuContainer)
        initAddFood(menuContainer)
        initMyRecipes(menuContainer)
        initLikeRecipes(menuContainer)
        initExit(menuContainer)
    }

    private fun initUserImage(view: View) {
        /*TODO set user img*/
    }

    private fun setUserName(view: View, userName: String) {
        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = userName
    }

    @SuppressLint("InflateParams")
    private fun initAddFood(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.add_black,
                R.string.add_recipes_menu_item)

        button.setOnClickListener { /*TODO onclick -> add*/ }
        container.addView(button)
    }

    @SuppressLint("InflateParams")
    private fun initMyRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.menu_black,
                R.string.my_recipes_menu_item)

        button.setOnClickListener { /*TODO onclick -> my*/ }
        container.addView(button)
    }

    @SuppressLint("InflateParams")
    private fun initLikeRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.like_black,
                R.string.like_recipes_menu_item)

        button.setOnClickListener { /*TODO onclick -> like*/ }
        container.addView(button)
    }

    @SuppressLint("InflateParams")
    private fun initExit(container: ViewGroup) {
        val button = createButton(
                R.layout.button_item_menu,
                R.drawable.arrow_back_black,
                R.string.exit_menu_item)

        button.setOnClickListener {
            //TODO dialog ok cancel
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        container.addView(button)
    }

    private fun createButton(resource: Int, @Nullable imgResource: Int, textResource: Int): Button {
        val button: Button = inflater?.inflate(resource, null) as Button
        button.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        button.text = getString(textResource)
        return button
    }
}
