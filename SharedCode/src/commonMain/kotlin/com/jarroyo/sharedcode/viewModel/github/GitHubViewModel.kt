/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.jarroyo.sharedcode.viewModel.github

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.di.KodeinInjector
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.domain.model.github.GithubIssue
import com.jarroyo.sharedcode.utils.coroutines.launchSilent
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

sealed class NetworkingState<T> {
    abstract val response: Response<T>?

    data class NetworkSuccess<T>(override val response: Response<T>): NetworkingState<T>()
    data class NetworkLoading<T>(override val response: Response<T>? = null): NetworkingState<T>()
    data class NetworkError<T>(override val response: Response<T>) : NetworkingState<T>()
}

class GitHubViewModel : ViewModel() {
    private val gitHubApi: GitHubApi by KodeinInjector.instance()

    var getGitHubRepoListLiveData = MutableLiveData<NetworkingState<List<GitHubRepo>>?>(null)
    var issuesList = MutableLiveData<NetworkingState<List<GithubIssue>>?>(null)

    // ASYNC - COROUTINES
    private val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun <T> trackingState(liveData: MutableLiveData<NetworkingState<T>?>, networking: suspend () -> Response<T>) = launchSilent(coroutineContext, exceptionHandler, job) {
        liveData.postValue(NetworkingState.NetworkLoading())
        val response = networking()
        when (response) {
            is Response.Success -> {
                liveData.postValue(NetworkingState.NetworkSuccess(response))
            }
            else -> {
                liveData.postValue(NetworkingState.NetworkError(response))
            }
        }
    }


    fun getGitHubRepoList(username: String) = launchSilent(coroutineContext, exceptionHandler, job) {
        trackingState(getGitHubRepoListLiveData) { gitHubApi.getGitHubRepoList(username) }
    }

    fun getIssuesList() = launchSilent(coroutineContext, exceptionHandler, job) {
        trackingState(issuesList) { gitHubApi.getIssuesList() }
    }

    fun setAuthToken(text: String) {
        gitHubApi.setToken(text)
    }

    val hasAuthToken: Boolean
        get() = gitHubApi.hasAuthToken
}