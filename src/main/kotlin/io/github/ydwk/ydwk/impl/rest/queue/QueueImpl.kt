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
package io.github.ydwk.ydwk.impl.rest.queue

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.impl.rest.enums.RestType
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.rest.queue.Queue
import okhttp3.RequestBody

class QueueImpl<T>(
    val ydwk: YDWK,
    private val requestBody: RequestBody,
    private val restType: RestType,
    private val endPoint: EndPoint.IEnumEndpoint
) : Queue<T> {

    override fun queue(): T {
        return queue(null)
    }

    override fun queue(success: ((T) -> Unit)?): T {
        return queue(success, null)
    }

    override fun queue(success: ((T) -> Unit)?, failure: ((Throwable) -> Unit)?): T {
        return ydwk.restApiManager.request(requestBody, restType, endPoint, success, failure)
    }
}
