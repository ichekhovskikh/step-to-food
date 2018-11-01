package com.sugar.steptofood.ui.fragment.user

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import android.widget.Toast
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
import com.sugar.steptofood.utils.FoodType
import com.sugar.steptofood.utils.hasPermissions
import com.sugar.steptofood.utils.requestPermissions
import com.sugar.steptofood.utils.showExitDialog
import kotlinx.android.synthetic.main.fragment_user.*
import javax.inject.Inject

open class UserFragment : UserView, BaseFragment() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService

    private val presenter by lazy { UserPresenter(this, api, session, context!!) }
    protected var userId: Int? = null

    companion object {
        fun getInstance() = UserFragment()

        const val PICK_IMAGE = 0
        const val PERMISSIONS_REQUEST_CODE = 1
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        App.appComponent.inject(this)

        userImageView.setOnClickListener {
            chooseImageIfHasPermissions()
        }

        initMenuItems(view)
        userId = activity!!.intent.getIntExtra(UID, session.userId)
        presenter.getUserName(userId!!)
        presenter.getAvatar(userId!!)
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
            intent.putExtra(ITEM_TYPE, FoodType.ADDED)
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
            intent.putExtra(ITEM_TYPE, FoodType.LIKE)
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

    private fun chooseImageIfHasPermissions() {
        if (hasPermissions(activity!!)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        } else {
            requestPermissions(activity!!, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && hasPermissions(activity!!)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = intent?.data!!
            userImageView.setImageURI(uri)
            presenter.setAvatar(uri)
        }
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}
