package com.sugar.steptofood.ui.fragment.user

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import com.sugar.steptofood.App
import com.sugar.steptofood.Session
import com.sugar.steptofood.presenter.UserPresenter
import com.sugar.steptofood.rest.ApiService
import com.sugar.steptofood.ui.activity.AddFoodActivity
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.activity.UserItemActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.view.UserView
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import com.sugar.steptofood.utils.ExtraName.UID
import com.sugar.steptofood.utils.showExitDialog
import kotlinx.android.synthetic.main.fragment_user.*
import javax.inject.Inject

open class UserFragment : UserView, BaseFragment() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { UserPresenter(this, api, session) }
    protected var userId: Int? = null

    companion object {
        fun getInstance() = UserFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        initMenuItems(view)
        userId = activity!!.intent.getIntExtra(UID, session.userId)
        presenter.getUserName(userId!!)
        presenter.getUserAvatar(userId!!)
    }

    override fun getLayout(): Int = R.layout.fragment_user

    open fun initMenuItems(view: View) {
        initAddFood(itemMenuContainer)
        initAddedRecipes(itemMenuContainer)
        initLikeRecipes(itemMenuContainer)
        initExit(itemMenuContainer)
    }

    override fun setUserName(name: String) {
        userNameTextView.text = name
    }

    override fun setUserAvatar(image: Bitmap) {
        userImageView.setImageBitmap(image)
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
            intent.putExtra(UID, userId)
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
            intent.putExtra(UID, userId)
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
            showExitDialog(context, ::exit)
        }
        container.addView(button)
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
        presenter.terminate()
        startActivity(intent)
    }
}
