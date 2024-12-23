package com.nphausg.loom

import com.nphausg.loom.internal.LoomImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.sample

interface Loom<T> {
    val state: StateFlow<LoomState<T>>
    fun refresh(fromUser: Boolean = false)
    fun update(modifier: (T) -> T)
}

sealed interface LoomSignal {
    data object Automatic : LoomSignal
    data object FromUser : LoomSignal
}

sealed interface LoomState<out T> {
    data object Loading : LoomState<Nothing>
    data object Init : LoomState<Nothing>
    data class Loaded<T>(val data: T) : LoomState<T>
    data class Error(val throwable: Throwable? = null) : LoomState<Nothing>
}

internal const val DEBOUNCE = 0L // Adjustable debounce period
internal const val THROTTLE = 0L // Adjustable throttle period

fun <T> Flow<T>.streamLine(
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE
): Flow<T> = this
    .debounce(debounceTimeMillis)
    // regular interval of updates where periodic updates are desired
    // regardless of how often new data is produced.
    .sample(throttleTimeMillis)

typealias StringInt = Loom<Int>
typealias StringLoom = Loom<String>
typealias LoomFactory<T> = (CoroutineScope, suspend (Boolean) -> T) -> Loom<T>

fun <T> loomIn(
    scope: CoroutineScope,
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE,
    execute: suspend (Boolean) -> T
): Loom<T> = LoomImpl(
    scope = scope,
    debounceTimeMillis = debounceTimeMillis,
    throttleTimeMillis = throttleTimeMillis,
    execute = execute
)
