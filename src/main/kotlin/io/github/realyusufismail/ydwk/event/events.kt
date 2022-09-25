/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.event

import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.event.recieve.CoroutineEventListener
import io.github.realyusufismail.ydwk.event.recieve.CoroutineEventReceiver
import kotlin.time.Duration

inline fun <reified T : Event> YDWK.on(
    timeout: Duration? = null,
    crossinline consumer: suspend CoroutineEventListener.(T) -> Unit
): CoroutineEventListener {
    println("Registering event listener for ${T::class.simpleName}")
    return (eventReceiver as CoroutineEventReceiver).onEvent(timeout, consumer)
}
