package com.cardes.stargithubuserexplorer

import androidx.annotation.IntegerRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not

fun checkViewVisible(viewId: Int) {
    onView(withId(viewId)).check(matches(isDisplayed()))
}

fun checkViewNotVisible(viewId: Int) {
    onView(withId(viewId)).check(matches(not(isDisplayed())))
}

fun checkViewWithTextVisible(text: String) {
    onView(withText(text)).check(matches(isDisplayed()))
}

fun checkViewWithTextVisible(resId: Int) {
    onView(withText(resId)).check(matches(isDisplayed()))
}

fun Int.assertVisibility(visible: Boolean) {
    if (visible) {
        checkViewVisible(this)
    } else {
        checkViewNotVisible(this)
    }
}
