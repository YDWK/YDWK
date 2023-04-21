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
package io.github.ydwk.ydwk

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.application.PartialApplication
import io.github.ydwk.yde.util.ThreadFactory
import java.util.concurrent.ScheduledExecutorService

/**
 * The main class of the YDWK library. This class is used to interact with the Discord API. Also
 * contains many things such as the embed builder for example.
 */
interface YDWK : YDE {
    /**
     * Gets some application properties sent by discord's Ready event.
     *
     * @return the [PartialApplication] object
     */
    var partialApplication: PartialApplication?

    /**
     * The properties of the application.
     *
     * @return the [Application] object
     */
    val application: Application?

    /**
     * The default ScheduledExecutorService.
     *
     * @return The [ScheduledExecutorService] object.
     */
    val defaultScheduledExecutorService: ScheduledExecutorService

    /**
     * The thread factory.
     *
     * @return The [ThreadFactory] object.
     */
    val threadFactory: ThreadFactory

    /**
     * Overrides the custom to string method.
     *
     * @return The string representation of the object.
     */
    override fun toString(): String
}
