package com.nphausg.loom.coroutine

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

sealed class LazyState<out T> {
    data object Uninitialized : LazyState<Nothing>()
    data class Initialized<T>(val value: T) : LazyState<T>()
    data class Failed(val exception: Throwable) : LazyState<Nothing>()
}

interface LazySuspend<T> : suspend () -> T {
    val isInitialized: Boolean
    fun valueOrNull(): T?
    override suspend operator fun invoke(): T
}

fun <T> lazySuspend(initializer: suspend () -> T): LazySuspend<T> {
    val state = AtomicReference<LazyState<T>>(LazyState.Uninitialized)
    val mutex = Mutex()

    return object : LazySuspend<T> {
        override val isInitialized: Boolean
            get() = state.get() is LazyState.Initialized

        override fun valueOrNull(): T? =
            (state.get() as? LazyState.Initialized)?.value

        override suspend fun invoke(): T {
            val currentState = state.get()
            if (currentState is LazyState.Initialized) {
                return currentState.value
            }

            return mutex.withLock {
                val doubleCheck = state.get()
                if (doubleCheck is LazyState.Initialized) {
                    return doubleCheck.value
                }

                try {
                    val result = initializer()
                    state.set(LazyState.Initialized(result))
                    result
                } catch (e: Throwable) {
                    state.set(LazyState.Failed(e))
                    throw e
                }
            }
        }
    }
}
