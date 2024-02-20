package io.github.ydwk.yde.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import org.jetbrains.annotations.BlockingExecutor
import java.util.concurrent.Executors

val Dispatchers.LOOM: @BlockingExecutor CoroutineDispatcher
    get() = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()