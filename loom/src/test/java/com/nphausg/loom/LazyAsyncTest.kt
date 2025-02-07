package com.nphausg.loom

import com.nphausg.loom.coroutine.lazyAsync
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

@ExperimentalCoroutinesApi
class LazyAsyncTest : BaseUnitTest() {

    private var job: Job? = null

    @Test
    fun `lazyAsync should initialize only once and return same Deferred`() = runTest {
        var callCount = 0

        val lazyValue by lazyAsync {
            callCount++
            "Hello, World!"
        }

        assertEquals(0, callCount) // Not initialized yet

        val result1 = lazyValue.await()
        val result2 = lazyValue.await()


        job?.cancel()
        job = launch {
            delay(200)
        }
        assertEquals(1, callCount) // Only initialized once
        assertEquals(result1, result2)
        assertEquals("Hello, World!", result1)
        assertEquals("Hello, World!", result2)
    }

    @Test
    fun `lazyAsync should execute asynchronously`() = runTest {
        val lazyValue by lazyAsync {
            delay(100) // Simulate async work
            "Async Result"
        }

        val deferred = lazyValue
        assertFalse(deferred.isCompleted) // Not completed yet

        val result = deferred.await()
        assertTrue(deferred.isCompleted) // Now completed
        assertEquals("Async Result", result)
    }

    @Test
    fun `lazyAsync should propagate exceptions correctly`() = runTest {
        val lazyValue by lazyAsync {
            throw IllegalStateException("Something went wrong")
        }

        try {
            lazyValue.await()
            fail("Exception should have been thrown")
        } catch (e: IllegalStateException) {
            assertEquals("Something went wrong", e.message)
        }
    }
}