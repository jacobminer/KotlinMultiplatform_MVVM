/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.jarroyo.sharedcode.viewModel.github

import com.jarroyo.kotlinmultiplatform.source.network.GitHubApi
import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.di.KodeinInjector
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.utils.coroutines.launchSilent
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

sealed class GetGitHubRepoListState {
    abstract val response: Response<List<GitHubRepo>>?
}
data class SuccessGetGitHubRepoListState(override val response: Response<List<GitHubRepo>>) : GetGitHubRepoListState()
data class LoadingGetGitHubRepoListState(override val response: Response<List<GitHubRepo>>? = null) : GetGitHubRepoListState()
data class ErrorGetGitHubRepoListState(override val response: Response<List<GitHubRepo>>) : GetGitHubRepoListState()

class GitHubViewModel : ViewModel() {
    private val gitGubApi: GitHubApi by KodeinInjector.instance()

    var getGitHubRepoListLiveData = MutableLiveData<GetGitHubRepoListState?>(null)

    // ASYNC - COROUTINES
    private val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun getGitHubRepoList(username: String) = launchSilent(coroutineContext, exceptionHandler, job) {
        getGitHubRepoListLiveData.postValue(LoadingGetGitHubRepoListState())
        val response = gitGubApi.getGitHubRepoList(username)
        if (response is Response.Success) {
            getGitHubRepoListLiveData.postValue(SuccessGetGitHubRepoListState(response))
        } else if (response is Response.Error) {
            getGitHubRepoListLiveData.postValue(ErrorGetGitHubRepoListState(response))
        }
    }
}