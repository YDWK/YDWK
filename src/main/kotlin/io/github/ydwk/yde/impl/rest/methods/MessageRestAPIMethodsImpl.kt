/*
 * Copyright 2023 YDWK inc.
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
package io.github.ydwk.yde.impl.rest.methods

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.methods.MessageRestAPIMethods
import io.github.ydwk.yde.rest.result.NoResult
import kotlinx.coroutines.CompletableDeferred

class MessageRestAPIMethodsImpl(val yde: YDE) : MessageRestAPIMethods {
    override fun deleteMessage(messageId: Long, channelId: Long): CompletableDeferred<NoResult> {
        return yde.restApiManager
            .delete(
                EndPoint.MessageEndpoint.DELETE_MESSAGE, channelId.toString(), messageId.toString())
            .executeWithNoResult()
    }
}
