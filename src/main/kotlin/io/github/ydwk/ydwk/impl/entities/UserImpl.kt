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
package io.github.ydwk.ydwk.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.channel.DmChannel
import io.github.ydwk.ydwk.impl.entities.channel.DmChannelImpl
import io.github.ydwk.ydwk.rest.EndPoint
import java.awt.Color
import java.util.*
import java.util.concurrent.CompletableFuture

open class UserImpl(
    final override val json: JsonNode,
    override val idAsLong: Long,
    override val ydwk: YDWK,
) : User {
    override var discriminator: String = json["discriminator"].asText()

    override var avatar: String = json["avatar"].asText()

    override val bot: Boolean
        get() = json.get("bot").asBoolean()

    override var system: Boolean? = if (json.has("system")) json["system"].asBoolean() else null

    override var mfaEnabled: Boolean? =
        if (json.has("mfa_enabled")) json["mfa_enabled"].asBoolean() else null

    // if null return null else return the string
    override var banner: String? =
        if (json.hasNonNull("banner")) json.get("banner").asText() else null

    override var accentColor: Color? =
        if (json.hasNonNull("accent_color")) Color(json.get("accent_color").asInt()) else null

    override var locale: String? =
        if (json.hasNonNull("locale")) json.get("locale").asText() else null

    override var verified: Boolean? =
        if (json.hasNonNull("verified")) json.get("verified").asBoolean() else null

    override var flags: Int? = if (json.hasNonNull("flags")) json.get("flags").asInt() else null

    override var premiumType: Int? =
        if (json.hasNonNull("premium_type")) json.get("premium_type").asInt() else null

    override var publicFlags: Int? =
        if (json.hasNonNull("public_flags")) json.get("public_flags").asInt() else null
    override val createDmChannel: CompletableFuture<DmChannel>
        get() {
            return ydwk.restApiManager
                .addQueryParameter("recipient_id", id)
                .post(null, EndPoint.UserEndpoint.CREATE_DM)
                .execute { it ->
                    val jsonBody = it.jsonBody
                    if (jsonBody == null) {
                        throw IllegalStateException("json body is null")
                    } else {
                        DmChannelImpl(ydwk, jsonBody, jsonBody["id"].asLong())
                    }
                }
        }

    override var name: String = json["username"].asText()

    override fun formatTo(formatter: Formatter?, flags: Int, width: Int, precision: Int) {
        formatter?.format("%s#%s", name, discriminator)
    }
}
