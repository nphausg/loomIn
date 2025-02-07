package com.nphausg.loom.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

// Extension function for easier usage of LazyAsync
fun <T> lazyAsync(initializer: suspend () -> T): Lazy<Deferred<T>> =
    lazy { CoroutineScope(Dispatchers.IO).async { initializer() } }
