package com.sugar.steptofood

import android.support.test.runner.AndroidJUnit4
import com.sugar.steptofood.ui.activity.UserActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserActivityTest : AcceptanceTest<UserActivity>(UserActivity::class.java) {

    @Test
    fun shouldDisplayMenuItems() {
        checkThat.viewIsVisibleAndContainsText(R.string.added_recipes_menu_item)
        checkThat.viewIsVisibleAndContainsText(R.string.like_recipes_menu_item)
    }
}