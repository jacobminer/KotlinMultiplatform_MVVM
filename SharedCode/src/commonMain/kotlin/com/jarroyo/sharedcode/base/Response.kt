package com.jarroyo.sharedcode.base

sealed class Response<out T> {
    class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Throwable,
                     val code: Int? = null,
                     val error: Boolean? = null,
                     val message: String? = null,
                     val method: String? = null,
                     val path: String? = null) : Response<Nothing>()
}