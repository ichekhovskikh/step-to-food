package com.sugar.steptofood

import android.support.test.runner.AndroidJUnit4
import com.sugar.steptofood.ui.activity.TabsActivity

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TabsActivityTest : AcceptanceTest<TabsActivity>(TabsActivity::class.java) {

    @Test
    fun shouldDisplayTabsName() {
        checkThat.viewIsVisibleAndContainsText(R.string.tabRecipesText)
        checkThat.viewIsVisibleAndContainsText(R.string.tabComposeText)
        checkThat.viewIsVisibleAndContainsText(R.string.tabUserText)
    }
}