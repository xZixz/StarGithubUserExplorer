package com.cardes.stargithubuserexplorer.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle

sealed class NavEvent(val clazz: Class<*>, val bundle: Bundle?)

class ActivityNavEvent(clazz: Class<*>, bundle: Bundle?) : NavEvent(clazz, bundle) {
    fun buildIntent(activity: Activity): Intent {
        return Intent(activity, clazz).apply {
            bundle?.let {
                putExtras(it)
            }
        }
    }
}

