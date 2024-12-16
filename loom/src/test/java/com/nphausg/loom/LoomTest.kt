package com.nphausg.loom

import com.nphausg.loom.internal.LoomImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// Reusable JUnit4 TestRule to override the Main dispatcher
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@RunWith(MockitoJUnitRunner::class)
class LoomTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var loom: LoomImpl<String>

    private val executeMock: suspend (Boolean) -> String = mock()

    @Before
    fun setup() {
        // Mocking the execute function
        runTest {
            whenever(executeMock(true)).thenReturn("User data")
            whenever(executeMock(false)).thenReturn("Automatic data")
        }
        val scope = TestScope(mainDispatcherRule.testDispatcher)
        loom = LoomImpl(
            scope = scope,
            execute = executeMock
        )
    }

    @Test
    fun `test state emits loading and loaded`() = runTest {
        // Collecting the state flow
        val states = mutableListOf<LoomState<String>>()
        val job = launch {
            loom.state.collect { states.add(it) }
        }

        // Triggering refresh (this should make the state flow through Loading -> Loaded)
        loom.refresh(fromUser = true)

        // Emitting data by simulating the flow
        advanceTimeBy(100) // Simulate some time passing for emissions

        // Checking that the state contains Loading and Loaded with the correct data
        assertEquals(2, states.size)
        assertTrue(states[0] is LoomState.Loading)
        assertTrue(states[1] is LoomState.Loaded)
        assertEquals("User data", (states[1] as LoomState.Loaded).data)

        // Cleanup
        job.cancel()
    }

    @Test
    fun `test refresh method triggers correct state changes`() = runTest {
        val states = mutableListOf<LoomState<String>>()
        val job = launch {
            loom.state.collect { states.add(it) }
        }

        // Call refresh method
        loom.refresh(fromUser = false)

        // Simulate the state update
        advanceTimeBy(100)

        // Check if the state reflects the expected values
        assertTrue(states[0] is LoomState.Loading)
        assertTrue(states[1] is LoomState.Loaded)
        assertEquals("Automatic data", (states[1] as LoomState.Loaded).data)

        job.cancel()
    }

    @Test
    fun `test update method applies modifier to state`() = runTest {
        val states = mutableListOf<LoomState<String>>()
        val job = launch {
            loom.state.collect { states.add(it) }
        }

        // Initial state (Loading)
        loom.refresh(fromUser = true)
        advanceTimeBy(100)

        // Apply modifier
        loom.update { it.uppercase() }

        // Simulate the state update with modifier
        advanceTimeBy(100)

        // Check that the data has been modified (to uppercase)
        assertTrue(states[1] is LoomState.Loaded)
        assertEquals("USER DATA", (states[1] as LoomState.Loaded).data)

        job.cancel()
    }

    @Test
    fun `test error handling in state`() = runTest {
        val states = mutableListOf<LoomState<String>>()
        val job = launch {
            loom.state.collect { states.add(it) }
        }

        // Mock the execute function to throw an error
        whenever(executeMock(true)).thenThrow(RuntimeException("Error"))

        loom.refresh(fromUser = true)
        advanceTimeBy(100)

        // Check that error state is emitted
        assertTrue(states.last() is LoomState.Error)

        job.cancel()
    }
}