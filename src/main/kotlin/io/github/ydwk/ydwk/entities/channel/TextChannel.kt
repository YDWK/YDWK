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
package io.github.ydwk.ydwk.entities.channel

import io.github.ydwk.ydwk.entities.Channel
import io.github.ydwk.ydwk.entities.channel.extender.TextChannelExtender
import io.github.ydwk.ydwk.entities.message.Embed
import java.util.concurrent.CompletableFuture

/**
 * Contains all the functions that are common to all 'text' channels (i.e. channels that can send
 * messages).
 */
interface TextChannel<T : TextChannelExtender> : Channel, TextChannelExtender {
    /**
     * Used to send a message to this channel.
     *
     * @param message the message to send.
     */
    fun sendMessage(message: String): CompletableFuture<T>

    /**
     * Used to send an embed to this channel.
     *
     * @param embed the embed to send.
     */
    fun sendEmbed(embed: Embed): CompletableFuture<T>
}
