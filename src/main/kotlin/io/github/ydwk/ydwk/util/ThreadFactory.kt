/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadFactory {
    private fun createThread(name: String, daemon: Boolean, block: Runnable): Thread {
        val thread = Thread(block)
        thread.name = name
        thread.isDaemon = daemon
        return thread
    }

    private fun getThreadByName(name: String): Thread? {
        return Thread.getAllStackTraces().keys.firstOrNull { it.name == name }
    }

    fun createThreadExecutor(name: String): ExecutorService {
        return Executors.newSingleThreadExecutor { r -> createThread(name, true, r) }
    }

    fun getThreadExecutorByName(threadName: String): ExecutorService? {
        val thread = getThreadByName(threadName)
        return if (thread != null) {
            Executors.newSingleThreadExecutor { thread }
        } else {
            null
        }
    }
}
