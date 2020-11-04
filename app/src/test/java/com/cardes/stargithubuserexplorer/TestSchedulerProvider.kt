package com.cardes.stargithubuserexplorer

import com.cardes.stargithubuserexplorer.common.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

    override fun mainThread(): Scheduler = Schedulers.trampoline()
}