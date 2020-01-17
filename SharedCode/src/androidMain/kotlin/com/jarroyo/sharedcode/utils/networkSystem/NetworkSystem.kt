package com.jarroyo.sharedcode.utils.networkSystem

import android.content.Context
import android.net.ConnectivityManager
import com.jarroyo.sharedcode.App

actual fun isNetworkAvailable(): Boolean{

    val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting

}