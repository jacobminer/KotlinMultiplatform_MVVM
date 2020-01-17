package com.jarroyo.sharedcode.di

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.ApplicationDispatcher
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
val KodeinInjector = Kodein {
    bind<CoroutineContext>() with provider { ApplicationDispatcher }
    bind<GitHubApi>() with provider { GitHubApi() }
}