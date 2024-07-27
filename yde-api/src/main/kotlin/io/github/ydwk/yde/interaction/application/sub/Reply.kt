/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.interaction.application.sub

import io.github.ydwk.yde.entities.interaction.actionrow.ActionRow
import io.github.ydwk.yde.entities.interaction.actionrow.creator.ActionRowCreator
import io.github.ydwk.yde.entities.message.MessageFlag
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.result.NoResult

/** Represents an object that can be used to reply to an interaction. */
interface Reply {

    /**
     * Sets the reply as an ephemeral message.
     *
     * @param ephemeral Whether the reply is ephemeral.
     * @return The [Reply] instance.
     */
    fun setEphemeral(isEphemeral: Boolean): Reply

    /**
     * Sets the reply as a 'text to speech' message.
     *
     * @param isTTS Whether the message should be 'text to speech'.
     * @return The [Reply] instance.
     */
    fun setTTS(isTTS: Boolean): Reply

    /**
     * Adds any other message flag to the reply.
     *
     * @param flags The flags to add.
     * @return The [Reply] instance.
     */
    fun addFlags(vararg flags: MessageFlag): Reply

    /**
     * Adds any other message flag to the reply.
     *
     * @param flags The flags to add.
     * @return The [Reply] instance.
     */
    fun addFlags(flags: List<MessageFlag>): Reply

    /**
     * Adds an [ActionRow] to the reply. Use [ActionRowCreator] to create an [ActionRow].
     *
     * @param actionRow The [ActionRow] to add.
     * @return The [Reply] instance.
     */
    fun addActionRow(actionRow: ActionRow): Reply

    /**
     * Triggers the reply.
     *
     * @return The [NoResult] instance.
     */
    @Deprecated("Use send instead", ReplaceWith("send()"), DeprecationLevel.WARNING)
    suspend fun trigger(): NoResult {
        return triggerWithFuture().mapBoth({ it }, { throw it })
    }

    /**
     * Replies and returns a `CompletableDeferred<NoResult>` that will be completed when the reply
     * is triggered.
     *
     * @return The `CompletableDeferred<NoResult>` instance.
     */
    @Deprecated(
        "Use sendWithFuture instead", ReplaceWith("sendWithFuture()"), DeprecationLevel.WARNING)
    suspend fun triggerWithFuture(): RestResult<NoResult>

    /**
     * Sends the reply.
     *
     * @return The [NoResult] instance.
     */
    suspend fun send(): NoResult {
        return sendWithFuture().mapBoth(onSuccess = { it }, onError = { throw it })
    }

    /**
     * Replies and returns a `CompletableDeferred<NoResult>` that will be completed when the reply
     * is sent.
     *
     * @return The `CompletableDeferred<NoResult>` instance.
     */
    suspend fun sendWithFuture(): RestResult<NoResult>
}
