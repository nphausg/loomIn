package com.nphausg.loom

import com.nphausg.loom.coroutine.lazySuspend
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class LazySuspendTest : BaseUnitTest() {

    @Test
    fun should_produce_value() = runTest {
        val lazyValue = lazySuspend { delay(1000); 123 }
        assertEquals(123, lazyValue())
        assertEquals(1000, currentTime)
    }

    @Test
    fun should_not_recalculate_value() = runTest {
        var next = 1
        val lazyValue = lazySuspend { delay(1000); next++ }
        assertEquals(1, lazyValue())
        assertEquals(1, lazyValue())
        assertEquals(1, lazyValue())
        assertEquals(1, lazyValue())
        assertEquals(1000, currentTime)
    }

    @Test
    fun should_not_calculate_value_multiple_times_when_multiple_coroutines_access_it() =
        runBlocking {
            var calculatedTimes = 0
            val lazyValue = lazySuspend { delay(1000); calculatedTimes++ }
            coroutineScope {
                repeat(10_000) {
                    launch {
                        lazyValue()
                    }
                }
            }
            assertEquals(1, calculatedTimes)
        }

    @Test
    fun should_try_again_when_failure_during_value_initialization() = runTest {
        var next = 0
        val lazyValue = lazySuspend {
            val v = next++
            if (v < 2) throw Error()
            v
        }
        assertTrue(runCatching { lazyValue() }.isFailure)
        assertTrue(runCatching { lazyValue() }.isFailure)
        assertEquals(2, lazyValue())
        assertEquals(2, lazyValue())
        assertEquals(2, lazyValue())
    }

    @Test
    fun should_use_context_of_the_first_caller() = runTest {
        var ctx: CoroutineContext? = null
        val lazyValue = lazySuspend {
            ctx = currentCoroutineContext()
            123
        }
        val name1 = CoroutineName("ABC")
        withContext(name1) {
            lazyValue()
        }
        assertEquals(name1, ctx?.get(CoroutineName))
        val name2 = CoroutineName("DEF")
        withContext(name2) {
            lazyValue()
        }
        assertEquals(name1, ctx?.get(CoroutineName))
    }

    @Test
    fun should_set_is_initialized() = runTest {
        val lazyValue = lazySuspend { delay(1000); 123 }

        assertEquals(false, lazyValue.isInitialized)
        launch { lazyValue() }
        assertEquals(false, lazyValue.isInitialized)
        advanceTimeBy(1000)
        assertEquals(false, lazyValue.isInitialized)
        runCurrent()
        assertEquals(true, lazyValue.isInitialized)
    }
}