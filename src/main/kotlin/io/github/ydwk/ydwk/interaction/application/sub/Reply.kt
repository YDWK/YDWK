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
package io.github.ydwk.ydwk.interaction.application.sub

interface Reply {

    /**
     * Sets the reply as an ephemeral message.
     *
     * @param ephemeral Whether the reply is ephemeral.
     * @return The [Reply] instance.
     */
    fun isEphemeral(isEphemeral: Boolean): Reply

    /**
     * Sets the reply as a 'text to speech' message.
     *
     * @param isTTS Whether the message should be 'text to speech'.
     * @return The [Reply] instance.
     */
    fun isTTS(isTTS: Boolean): Reply

    /**
     * Triggers the reply.
     *
     * @return The [Void] instance.
     */
    fun reply(): Void
}
