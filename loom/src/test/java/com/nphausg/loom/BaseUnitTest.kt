package com.nphausg.loom

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit [MainDispatcherRule] that sets the Main dispatcher to [testDispatcher]
 * for the duration of the test.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}


/**
 * @see <a href="https://developer.android.com/kotlin/coroutines/test"> Coroutines Test </a>
 * */
abstract class BaseUnitTest {
    
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Before
    open fun setUp() {
    }

    @After
    open fun tearDown() {
    }
}
