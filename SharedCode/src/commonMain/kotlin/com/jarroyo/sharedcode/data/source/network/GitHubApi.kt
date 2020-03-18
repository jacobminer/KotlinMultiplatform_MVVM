package com.jarroyo.kotlinmultiplatform.source.network


import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.base.exception.NetworkConnectionException
import com.jarroyo.sharedcode.base.exception.NotAuthenticatedException
import com.jarroyo.sharedcode.domain.model.github.GitHubRepo
import com.jarroyo.sharedcode.domain.model.github.GithubIssue
import com.jarroyo.sharedcode.utils.networkSystem.isNetworkAvailable
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.serialization.*
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json

class GitHubApi {
    private val httpClient = HttpClient()
    private var authToken: String = ""

    val hasAuthToken: Boolean
        get() = !authToken.isBlank()

    fun setToken(token: String) {
        authToken = token
    }

    @OptIn(ImplicitReflectionSerializer::class)
    private suspend inline fun <reified T: Any> get(endpoint: String, serializer: KSerializer<T> = T::class.serializer()): Response<T> {
        if (!isNetworkAvailable()) {
            return Response.Error(NetworkConnectionException())
        }

        if (authToken.isBlank()) {
            return Response.Error(NotAuthenticatedException())
        }

        return try {
            val url = "https://api.github.com/$endpoint"
            val json = httpClient.get<String> {
                url(url)
                headers.append("Authorization", "token $authToken")
            }
            val response = Json.nonstrict.parse(serializer, json)
            Response.Success(response)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    @OptIn(ImplicitReflectionSerializer::class)
    private suspend inline fun <reified T: Any> getList(endpoint: String): Response<List<T>> {
        return get(endpoint, T::class.serializer().list)
    }

    suspend fun getGitHubRepoList(username: String): Response<List<GitHubRepo>> {
        return getList("users/${username}/repos")
    }

    suspend fun getIssuesList(): Response<List<GithubIssue>> {
        return getList("issues")
    }
}
