package com.jarroyo.sharedcode

import android.app.Application

/**
 * KMP_MVVM
 * Created by jake on 2020-01-16, 4:42 PM
 */
open class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        lateinit var instance: App
    }
}