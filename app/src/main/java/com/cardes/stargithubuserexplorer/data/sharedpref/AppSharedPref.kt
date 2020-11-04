package com.cardes.stargithubuserexplorer.data.sharedpref

import android.content.Context
import javax.inject.Inject

interface AppSharedPref {
    fun setLatestCachedUpdateTime(loginName: String, time: Long)
    fun getLatestCachedUpdateTime(loginName: String): Long
}

class AppSharedPrefImpl @Inject constructor(context: Context) : AppSharedPref {

    private val sharedPref = context.getSharedPreferences(SHARED_REF_NAME, Context.MODE_PRIVATE)

    override fun setLatestCachedUpdateTime(loginName: String, time: Long) {
        sharedPref
            .edit()
            .putLong("${CACHE_UPDATE_TIME_KEY}_${loginName}", time)
            .apply()
    }

    override fun getLatestCachedUpdateTime(loginName: String): Long {
        return sharedPref.getLong("${CACHE_UPDATE_TIME_KEY}_${loginName}", 0)
    }

    companion object {
        const val SHARED_REF_NAME = "github_user_explorer_pref"
        const val CACHE_UPDATE_TIME_KEY = "cache_update_time"
    }
}