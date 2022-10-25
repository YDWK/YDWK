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
package io.github.ydwk.ydwk.eventmanager.handler.handlers.message

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.cache.CacheIds
import io.github.ydwk.ydwk.eventmanager.event.events.message.MessageDeleteBulkEvent
import io.github.ydwk.ydwk.eventmanager.handler.Handler
import io.github.ydwk.ydwk.impl.YDWKImpl
import io.github.ydwk.ydwk.impl.entities.MessageImpl

class MessageBulkDeleteHandler(ydwk: YDWKImpl, json: JsonNode) : Handler(ydwk, json) {
    override fun start() {
        val messages = json.get("ids").map { MessageImpl(ydwk, json, json.asLong()) }
        messages.forEach { ydwk.cache.remove(it.id, CacheIds.MESSAGE) }
        ydwk.emitEvent(MessageDeleteBulkEvent(ydwk, messages))
    }
}
