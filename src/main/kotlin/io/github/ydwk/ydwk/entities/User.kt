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
package io.github.ydwk.ydwk.entities

import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.entities.message.SendAble
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.impl.entities.channel.DmChannelImpl
import io.github.ydwk.ydwk.rest.EndPoint
import io.github.ydwk.ydwk.util.NameAbleEntity
import io.github.ydwk.ydwk.util.SnowFlake
import java.awt.Color
import java.util.concurrent.CompletableFuture
import okhttp3.RequestBody.Companion.toRequestBody

interface User : SnowFlake, GenericEntity, NameAbleEntity, SendAble {
    /**
     * The user's 4-digit discord-tag.
     *
     * @return the user's 4-digit discord-tag.
     */
    var discriminator: String

    /**
     * The user's avatar hash.
     *
     * @return the user's avatar hash.
     */
    var avatar: String

    /**
     * Whether the user belongs to an OAuth2 application.
     *
     * @return whether the user belongs to an OAuth2 application
     */
    val bot: Boolean?

    /**
     * Whether the user is an Official Discord System user (part of the urgent message system).
     *
     * @return whether the user is an Official Discord System user (part of the urgent message
     *   system).
     */
    var system: Boolean?

    /**
     * Whether the user has two factor enabled on their account.
     *
     * @return whether the user has two factor enabled on their account.
     */
    var mfaEnabled: Boolean?

    /**
     * The user's banner hash.
     *
     * @return the user's banner hash.
     */
    var banner: String?

    /**
     * The user's banner color encoded as an integer representation of hexadecimal color code.
     *
     * @return the user's banner color encoded as an integer representation of hexadecimal color
     *   code.
     */
    var accentColor: Color?

    /**
     * The user's chosen language option.
     *
     * @return the user's chosen language option.
     */
    var locale: String?

    /**
     * Whether the email on this account has been verified.
     *
     * @return whether the email on this account has been verified.
     */
    var verified: Boolean?

    /**
     * The flags on a user's account.
     *
     * @return the flags on a user's account.
     */
    var flags: Int?

    /**
     * The public flags on a user's account.
     *
     * @return the public flags on a user's account.
     */
    var publicFlags: Int?

    /**
     * Creates a dm channel with this user.
     *
     * @return a dm channel with this user.
     */
    val createDmChannel: CompletableFuture<DmChannel>
        get() {
            return ydwk.restApiManager
                .post(
                    ydwk.objectMapper
                        .createObjectNode()
                        .put("recipient_id", id)
                        .toString()
                        .toRequestBody(),
                    EndPoint.UserEndpoint.CREATE_DM)
                .execute {
                    val jsonBody = it.jsonBody
                    if (jsonBody == null) {
                        throw IllegalStateException("json body is null")
                    } else {
                        DmChannelImpl(ydwk, jsonBody, jsonBody["id"].asLong())
                    }
                }
        }
}
