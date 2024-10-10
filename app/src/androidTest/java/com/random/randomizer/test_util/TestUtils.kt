package com.random.randomizer.test_util

import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry

fun testStringResource(@StringRes id: Int): String {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    return context.getString(id)
}