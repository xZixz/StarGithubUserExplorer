package com.cardes.stargithubuserexplorer.common

import javax.inject.Inject

interface TimeProvider {
    fun getCurrentTime(): Long
}

class TimeProviderImpl @Inject constructor() : TimeProvider {
    override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}