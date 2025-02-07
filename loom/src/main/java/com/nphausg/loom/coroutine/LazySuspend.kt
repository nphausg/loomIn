package com.nphausg.loom.coroutine

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

/**
 * Sealed class representing the state of a lazily initialized value.
 * It can either be uninitialized, initialized with a value, or failed due to an exception.
 *
 * @param T The type of the value being lazily initialized.
 */
sealed class LazyState<out T> {
    /**
     * Represents an uninitialized state.
     */
    data object Uninitialized : LazyState<Nothing>()

    /**
     * Represents an initialized state holding a value of type [T].
     *
     * @param value The initialized value of type [T].
     */
    data class Initialized<T>(val value: T) : LazyState<T>()

    /**
     * Represents a failed state containing an exception.
     *
     * @param exception The exception that occurred during initialization.
     */
    data class Failed(val exception: Throwable) : LazyState<Nothing>()
}

/**
 * A functional interface representing a suspending function that can return a value of type [T].
 * This interface also provides properties and functions to check the initialization state and
 * retrieve the value if it's available.
 */
interface LazySuspend<T> : suspend () -> T {
    /**
     * A property indicating if the value has been initialized.
     */
    val isInitialized: Boolean

    /**
     * Returns the value if it's initialized, or `null` if it's uninitialized or failed.
     *
     * @return The initialized value of type [T] or `null`.
     */
    fun getOrNull(): T?

    /**
     * Suspends the execution and retrieves the lazily initialized value.
     * If not initialized, it will invoke the initializer function.
     *
     * @return The lazily initialized value of type [T].
     */
    override suspend operator fun invoke(): T
}

/**
 * Creates a [LazySuspend] instance, which lazily initializes a value using the provided [initializer].
 * The initialization is done in a thread-safe manner, using double-checked locking.
 *
 * If the initialization fails, the state will be marked as failed and the exception will be thrown.
 *
 * @param T The type of the lazily initialized value.
 * @param initializer A suspending function that provides the value to be lazily initialized.
 * @return A [LazySuspend] instance that encapsulates the lazy initialization logic.
 * @author <a href="https://github.com/nphausg">nphausg</>
 */
fun <T> lazySuspend(initializer: suspend () -> T): LazySuspend<T> {
    val state = AtomicReference<LazyState<T>>(LazyState.Uninitialized)
    val mutex = Mutex()

    return object : LazySuspend<T> {
        override val isInitialized: Boolean
            get() = state.get() is LazyState.Initialized

        override fun getOrNull(): T? =
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
