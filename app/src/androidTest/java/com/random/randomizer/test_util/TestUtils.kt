package com.random.randomizer.test_util

import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider.getApplicationContext

fun stringResource(@StringRes id: Int): String {
    val context = getApplicationContext<Context>()
    return context.getString(id)
}

fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    val context = getApplicationContext<Context>()
    return context.getString(id, *formatArgs)
}