package com.sugar.steptofood

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers


class Events {
    fun clickOnView(@IdRes viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
    }
}