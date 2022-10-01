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
package io.github.realyusufismail.bot

import io.github.realyusufismail.jconfig.util.JConfigUtils
import io.github.realyusufismail.ydwk.createDefaultBot
import io.github.realyusufismail.ydwk.event.ListenerAdapter
import io.github.realyusufismail.ydwk.event.backend.event.on
import io.github.realyusufismail.ydwk.event.events.ReadyEvent

object Bot : ListenerAdapter() {

    @JvmStatic
    fun main(args: Array<String>) {
        val ydwk = createDefaultBot(JConfigUtils.getString("token"))

        ydwk.addEvent(this)

        ydwk.on<ReadyEvent> { println("Ready!") }
    }

    override fun onReady(event: ReadyEvent) {
        println("Bot is ready!")
    }
}