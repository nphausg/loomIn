package com.nphausg.loom

import com.nphausg.loom.internal.LoomImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoomTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loom: LoomImpl<Int>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        loom = LoomImpl(
            scope = CoroutineScope(testDispatcher),
            debounceTimeMillis = 50L,
            throttleTimeMillis = 50L,
            execute = { isFromUser ->
                if (isFromUser) 42 else 24
            }
        )
    }

    @Test
    fun `initial state is LoomState_Init`() = runTest {
        assertEquals(LoomState.Init, loom.state.value)
    }

    @Test
    fun `refresh triggers automatic loading`() = runTest {
        val loom = loomIn(
            scope = CoroutineScope(testDispatcher),
            debounceTimeMillis = 50L,
            throttleTimeMillis = 50L,
            execute = { isFromUser ->
                if (isFromUser) 42 else 24
            }
        )
        loom.refresh(fromUser = false)
        advanceUntilIdle()
        assertEquals(LoomState.Init, loom.state.value)
    }

    @Test
    fun `refresh from user triggers user-specific loading`() = runTest {
        loom.refresh(fromUser = true)

        advanceUntilIdle()

        val result = loom.state.first()
        assertEquals(LoomState.Init, loom.state.value)
    }

    @Test
    fun `update modifies the current state data`() = runTest {
        loom.refresh(fromUser = false)
        advanceUntilIdle()

        loom.update { currentData ->
            currentData + 1
        }

        advanceUntilIdle()

        assertEquals(LoomState.Init, loom.state.value)
    }

    @Test
    fun `update without refresh does not modify initial state`() = runTest {
        loom.update { currentData -> currentData + 1 }

        advanceUntilIdle()

        assertEquals(LoomState.Init, loom.state.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}