package com.nphausg.loom

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The Loom class is a generic class in Kotlin designed to manage and execute a suspending function
 * within a CoroutineScope. It uses Kotlin's coroutines and Flow APIs to handle asynchronous operations
 * and state management. Here are the key components and functionalities of the Loom class:
 * @param scope: A CoroutineScope in which the coroutine will be launched.
 * @param execute: A suspending function that returns a value of type T.
 *
 *
 * Example:
 *
 * ```kotlin
 *  private val refresher = loomIn(viewModelScope) { getData() }
 *  val uiState: StateFlow<UiState<String>> = refresher.state
 *  private suspend fun getData(): String {
 *         delay(1_000)
 *         return "Loom"
 *  }
 *
 *  fun refresh(){
 *      refresher.refresh()
 *  }
 */
class Loom<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val debounceTimeMillis: Long = DEBOUNCE,
    private val throttleTimeMillis: Long = THROTTLE,
    private val execute: suspend () -> T
) {

    private val _state = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val state: StateFlow<UiState<T>> = _state
        .streamLine(debounceTimeMillis, throttleTimeMillis)
        .onStart { emit(Unit) } // Emit the first refresh automatically
        .flatMapLatest {
            flow {
                emit(UiState.Loading)
                try {
                    val data = execute()
                    emit(UiState.Loaded(data))
                } catch (e: Exception) {
                    emit(UiState.Error(e))
                }
            }
        }
        .catch { emit(UiState.Error(it)) }
        .stateIn(scope, SharingStarted.Lazily, UiState.Init)

    fun refresh() {
        scope.launch { _state.emit(Unit) }
    }
}

private const val DEBOUNCE = 200L // Adjustable debounce period
private const val THROTTLE = 1_000L // Adjustable throttle period
private fun <T> Flow<T>.streamLine(
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE
): Flow<T> = this
    .debounce(debounceTimeMillis)
    // regular interval of updates where periodic updates are desired
    // regardless of how often new data is produced.
    .sample(throttleTimeMillis)

fun <T> loomIn(
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    debounceTimeMillis: Long = DEBOUNCE,
    throttleTimeMillis: Long = THROTTLE,
    execute: suspend () -> T
) = Loom(
    scope = scope,
    debounceTimeMillis = debounceTimeMillis,
    throttleTimeMillis = throttleTimeMillis,
    execute = execute
)
