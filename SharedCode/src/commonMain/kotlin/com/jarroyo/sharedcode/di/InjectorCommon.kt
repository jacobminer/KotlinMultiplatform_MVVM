package com.jarroyo.sharedcode.di

import com.jarroyo.sharedcode.utils.networkSystem.ContextArgs
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object InjectorCommon {

    lateinit var contextArgs: ContextArgs

    fun provideContextArgs(contextArgs: ContextArgs): ContextArgs {
        this.contextArgs = contextArgs
        return contextArgs
    }


}
