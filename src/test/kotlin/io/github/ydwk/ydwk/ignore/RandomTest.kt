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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk.ignore

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import kotlin.reflect.jvm.jvmName
import org.junit.jupiter.api.Test

class RandomTest {
    @Test
    fun test() {
        // How can i do something like where for every 6 of something do function wait 2 seconds and
        // repeat until list is empty (edited)

        val list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val listChunks = list.chunked(6)

        for (chunk in listChunks) {
            for (i in chunk) {
                println(i)
            }
            Thread.sleep(2000)
        }

        println("Done")
    }

    @Test
    fun test2() {
        println(GenericEvent::class.jvmName)
    }
}
