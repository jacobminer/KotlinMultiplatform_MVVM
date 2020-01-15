package com.jarroyo.kmp_mvvm

import androidx.compose.effectOf
import androidx.compose.memo
import androidx.compose.onCommit
import androidx.compose.state
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import dev.icerock.moko.mvvm.livedata.MutableLiveData

/**
 * KMP_MVVM
 * Created by jake on 2020-01-15, 11:15 AM
 */
//fun <T> observe(data: LiveData<T>) = effectOf<T?> {
//    val result = +state { data.value }
//    val observer = +memo { Observer<T> { result.value = it } }
//
//    +onCommit(data) {
//        data.observeForever(observer)
//        onDispose { data.removeObserver(observer) }
//    }
//
//    result.value
//}

fun <T> observe(data: MutableLiveData<T>) = effectOf<T?> {
    val result = +state { data.value }
    val observerFunction: (T) -> Unit = { result.value = it }
    val observer = +memo { observerFunction }

    +onCommit(data) {
        data.addObserver(observer)
        onDispose { data.removeObserver(observer) }
    }

    result.value
}