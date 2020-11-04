package com.cardes.stargithubuserexplorer

import android.app.Activity
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.rule.ActivityTestRule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.DispatchingAndroidInjector_Factory
import javax.inject.Provider

class DaggerActivityTestRule<T : Activity>(
    private val activityClass: Class<T>,
    initialTouchMode: Boolean,
    launchActivity: Boolean,
    private val setupMocks: (T) -> Unit
) : ActivityTestRule<T>(activityClass, initialTouchMode, launchActivity) {
    @Suppress("DEPRECATION")
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val app = getTargetContext().applicationContext as MyApp
        app.dispatchingAndroidInjector =
            createFakeActivityInjector(activityClass, setupMocks)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createFakeActivityInjector(
        clazz: Class<T>,
        setupMocks: (T) -> Unit
    ): DispatchingAndroidInjector<Any> {
        val injector = AndroidInjector<Any> { instance ->
            if (instance.javaClass == clazz) {
                setupMocks(instance as T)
            }
        }
        val factory = AndroidInjector.Factory<Any> { injector }
        val map =
            mapOf(Pair<Class<*>, Provider<AndroidInjector.Factory<*>>>(clazz, Provider { factory }))
        return DispatchingAndroidInjector_Factory.newInstance(map, emptyMap())
    }
}
