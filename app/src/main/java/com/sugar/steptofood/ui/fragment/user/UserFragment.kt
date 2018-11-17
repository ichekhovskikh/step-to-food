package com.sugar.steptofood.ui.fragment.user

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sugar.steptofood.R
import android.support.annotation.Nullable
import android.widget.Toast
import com.sugar.steptofood.extension.observe
import com.sugar.steptofood.repository.BaseRepository
import com.sugar.steptofood.ui.activity.RecipeCreationActivity
import com.sugar.steptofood.ui.activity.StartActivity
import com.sugar.steptofood.ui.activity.RecipeListActivity
import com.sugar.steptofood.ui.fragment.BaseFragment
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import com.sugar.steptofood.utils.*
import com.sugar.steptofood.utils.ExtraName.ITEM_TYPE
import com.sugar.steptofood.utils.ExtraName.UID
import kotlinx.android.synthetic.main.fragment_user.*

open class UserFragment : BaseFragment() {

    private val userViewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }

    companion object {
        fun getInstance() = UserFragment()

        const val PICK_IMAGE = 0
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        userImageView.setOnClickListener {
            userAvatarClickListener()
        }
        initMenuItems(view)
        initUserContent(getCurrentUserId())
        initLoader()
        initErrorObserver()
    }

    fun getCurrentUserId() = activity!!.intent.getIntExtra(UID, userViewModel.session.userId)

    open fun userAvatarClickListener() {
        chooseImageIfHasPermissions()
    }

    override fun getLayout(): Int = R.layout.fragment_user

    open fun initMenuItems(view: View) {
        initAddRecipe(itemMenuContainer)
        initAddedRecipes(itemMenuContainer)
        initLikeRecipes(itemMenuContainer)
        initExit(itemMenuContainer)
    }

    private fun initUserContent(userId: Int) {
        userViewModel.getUserName(userId).observe(this) { name ->
            setUserName(name)
        }
        loadAvatar(userId).into(userImageView)
    }

    private fun setUserName(name: String) {
        userNameTextView?.text = name
    }

    fun initAddRecipe(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
                R.drawable.add_black,
                R.string.add_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, RecipeCreationActivity::class.java)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initAddedRecipes(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
                R.drawable.menu_black,
                R.string.added_recipes_menu_item)

        button.setOnClickListener {
            val intent = Intent(activity, RecipeListActivity::class.java)
            intent.putExtra(UID, getCurrentUserId())
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
            intent.putExtra(UID, getCurrentUserId())
            intent.putExtra(ITEM_TYPE, RecipeType.LIKE)
            startActivity(intent)
        }
        container.addView(button)
    }

    fun initExit(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
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
        userViewModel.session.reset()
        startActivity(intent)
    }

    private fun chooseImageIfHasPermissions() {
        if (hasStoragePermissions(activity!!)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        } else {
            requestStoragePermissions(activity!!, OPEN_GALLERY_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri = intent?.data!!
            userImageView.setImageURI(uri)
            userViewModel.setAvatar(uri)
        }
    }

    private fun initLoader() {
        userViewModel.getLoadingStatus().observe(this) { status ->
            when (status) {
                BaseRepository.LoadingStatus.LOADING -> showLoading()
                BaseRepository.LoadingStatus.LOADED -> hideLoading()
            }
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun initErrorObserver() {
        userViewModel.getErrorMessage().observe(this) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }
}
