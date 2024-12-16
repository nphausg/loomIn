package com.nphausg.loom.internal

import com.nphausg.loom.DEBOUNCE
import com.nphausg.loom.Loom
import com.nphausg.loom.LoomSignal
import com.nphausg.loom.LoomState
import com.nphausg.loom.THROTTLE
import com.nphausg.loom.streamLine
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * The Loom class is a generic class in Kotlin designed to manage and execute a suspending function
 * within a CoroutineScope. It uses Kotlin's coroutines and Flow APIs to handle asynchronous operations
 * and state management. Here are the key components and functionalities of the Loom class:
 * @param scope: A CoroutineScope in which the coroutine will be launched.
 * @param execute: A suspending function that returns a value of type T.
 * @author https://github.com/nphausg/loom
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
internal class LoomImpl<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val debounceTimeMillis: Long = DEBOUNCE,
    private val throttleTimeMillis: Long = THROTTLE,
    private val execute: suspend (Boolean) -> T
) : Loom<T> {
    private val mutex = Mutex();
    private val _modifier = MutableSharedFlow<(T) -> T>(extraBufferCapacity = 1)
    private val _state = MutableSharedFlow<LoomSignal>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val state: StateFlow<LoomState<T>> = _state
        .streamLine(debounceTimeMillis, throttleTimeMillis)
        .onStart { emit(LoomSignal.Automatic) } // Emit the first refresh automatically
        .flatMapLatest { event ->
            flow {
                emit(LoomState.Loading)
                try {
                    val data = execute(event is LoomSignal.FromUser)
                    emit(LoomState.Loaded(data))
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    emit(LoomState.Error(e))
                }
            }
        }
        .combine(_modifier.onStart { emit { data: T -> data } }) { oldState, modifier ->
            mutex.withLock {
                if (oldState is LoomState.Loaded) {
                    val expectedData = modifier(oldState.data)
                    if (expectedData != oldState.data) {
                        LoomState.Loaded(expectedData)
                    } else {
                        oldState // Don't do anything if value is not changing
                    }
                } else {
                    oldState // Don't do anything if value is not changing
                }
            }
        }
        .catch { emit(LoomState.Error(it)) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000,
                replayExpirationMillis = 0
            ),
            initialValue = LoomState.Init
        )

    override fun refresh(fromUser: Boolean) {
        _state.subscriptionCount.value > 0
        _state.tryEmit(if (fromUser) LoomSignal.FromUser else LoomSignal.Automatic)
    }

    override fun update(modifier: (T) -> T) {
        _modifier.tryEmit(modifier)
    }
}
