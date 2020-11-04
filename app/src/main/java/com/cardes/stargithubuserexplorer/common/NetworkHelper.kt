package com.cardes.stargithubuserexplorer.common

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import timber.log.Timber
import javax.inject.Inject


interface NetworkHelper {
    fun isNetworkAvailable(): Boolean
}

class NetworkHelperImpl @Inject constructor(
    private val context: Context
) : NetworkHelper {

    override fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isNetworkAvailableAndroidQ()
        } else {
            isNetworkAvailablePreQ()
        }
    }


    @TargetApi(Build.VERSION_CODES.Q)
    private fun isNetworkAvailableAndroidQ(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }

        return false
    }

    private fun isNetworkAvailablePreQ(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        try {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            Timber.e(e, "Check network available failed")
        }

        return false
    }

    companion object {
        private const val TAG = "NetworkHelperImpl"
    }
}
