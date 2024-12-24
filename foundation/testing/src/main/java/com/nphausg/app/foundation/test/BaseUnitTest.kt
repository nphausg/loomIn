package com.nphausg.app.foundation.test

import com.nphausg.app.foundation.test.rule.MainDispatcherRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

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
