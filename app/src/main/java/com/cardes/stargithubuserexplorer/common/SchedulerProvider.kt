package com.cardes.stargithubuserexplorer.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun mainThread(): Scheduler
}

class SchedulerProviderImpl @Inject constructor(): SchedulerProvider {
    override fun io(): Scheduler {
         return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}