package com.sugar.steptofood

import android.content.Intent
import android.provider.MediaStore
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.runner.AndroidJUnit4
import com.sugar.steptofood.ui.activity.RecipeCreationActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeCreationActivityTest : AcceptanceTest<RecipeCreationActivity>(RecipeCreationActivity::class.java) {

    @Test
    fun shouldDisplayImageChooser() {
        events.clickOnView(R.id.userImageView)
        intended(IntentMatchers.hasData(MediaStore.Images.Media.INTERNAL_CONTENT_URI))
        intended(IntentMatchers.hasAction(Intent.ACTION_PICK))
    }
}