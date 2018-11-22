package com.sugar.steptofood.ui.fragment.user

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import android.widget.Toast
import com.sugar.steptofood.utils.extension.observe
import com.sugar.steptofood.ui.activity.RecipeListActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import com.sugar.steptofood.utils.*
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import com.sugar.steptofood.utils.ExtraName.UID
import kotlinx.android.synthetic.main.fragment_user.*

abstract class BaseUserFragment : BaseFragment() {

    private val userViewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }
    private val currentUserId by lazy { activity!!.intent.getIntExtra(UID, userViewModel.session.userId) }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        userImageView.setOnClickListener {
            userAvatarClickListener()
        }
        initMenuItems(view)
        initUserContent(currentUserId)
        initLoader()
    }

    open fun userAvatarClickListener() {}

    override fun getLayout(): Int = R.layout.fragment_user

    abstract fun initMenuItems(view: View)

    fun initAddedRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
                R.drawable.menu_black,
                R.string.added_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, RecipeListActivity::class.java)
            intent.putExtra(UID, currentUserId)
            intent.putExtra(ITEM_TYPE, RecipeType.ADDED)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initLikeRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
                R.drawable.like_black,
                R.string.like_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, RecipeListActivity::class.java)
            intent.putExtra(UID, currentUserId)
            intent.putExtra(ITEM_TYPE, RecipeType.LIKE)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun createButton(resource: Int, @Nullable imgResource: Int, textResource: Int): Button {
        val button: Button = inflater?.inflate(resource, null) as Button
        button.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0)
        button.text = getString(textResource)
        return button
    }

    private fun initUserContent(userId: Int) {
        userViewModel.getUserName(userId).observe(this) { name ->
            setUserName(name)
        }
        loadAvatar(userId)?.into(userImageView)
    }

    private fun setUserName(name: String) {
        userNameTextView?.text = name
    }

    private fun initLoader() {
        userViewModel.getLoadingStatus().observe(this) { networkState ->
            when (networkState.status) {
                Status.RUNNING -> showLoading()
                Status.SUCCESS -> hideLoading()
                Status.FAILED -> showError(networkState.msg)
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
