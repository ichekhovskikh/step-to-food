package com.sugar.steptofood.ui.fragment.user

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import com.sugar.steptofood.R
import com.sugar.steptofood.ui.activity.*
import com.sugar.steptofood.ui.viewmodel.UserViewModel
import com.sugar.steptofood.utils.*
import kotlinx.android.synthetic.main.fragment_user.*

open class SessionUserFragment : BaseUserFragment() {

    private val userViewModel by lazy { ViewModelProviders.of(this).get(UserViewModel::class.java) }

    companion object {
        fun getInstance() = SessionUserFragment()

        const val PICK_IMAGE = 0
    }

    override fun userAvatarClickListener() {
        chooseImageIfHasPermissions()
    }

    override fun initMenuItems(view: View) {
        initAddRecipe(itemMenuContainer)
        initAddedRecipes(itemMenuContainer)
        initLikeRecipes(itemMenuContainer)
        initExit(itemMenuContainer)
    }

    private fun initAddRecipe(container: ViewGroup) {
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

    private fun initExit(container: ViewGroup) {
        val button = createButton(
                R.layout.button_menu,
                R.drawable.arrow_back_black,
                R.string.exit_menu_item)

        button.setOnClickListener {
            showExitDialog(context, ::exit)
        }
        container.addView(button)
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
}
