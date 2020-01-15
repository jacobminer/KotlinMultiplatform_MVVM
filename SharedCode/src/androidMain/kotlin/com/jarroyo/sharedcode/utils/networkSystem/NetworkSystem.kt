package com.jarroyo.sharedcode.utils.networkSystem

import android.content.Context
import android.net.ConnectivityManager
import com.jarroyo.sharedcode.di.InjectorCommon

actual class ContextArgs(
    var context: Context
)

actual fun isNetworkAvailable(): Boolean{
    val connectivityManager = InjectorCommon.contextArgs.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting

}