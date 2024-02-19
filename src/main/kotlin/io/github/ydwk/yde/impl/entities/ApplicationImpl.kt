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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.util.EntityToStringBuilder
import io.github.ydwk.yde.util.GetterSnowFlake
import java.net.URL

class ApplicationImpl(
    override val json: JsonNode,
    override val idAsLong: Long,
    override val yde: YDE,
) : Application {

    override var icon: String? = if (json.hasNonNull("icon")) json["icon"].asText() else null

    override var description: String = json["description"].asText()

    override var rpcOrigins: Array<String>? =
        if (json.hasNonNull("rpc_origins")) json["rpc_origins"].asText().split(",").toTypedArray()
        else null

    override var botPublic: Boolean = json["bot_public"].asBoolean()

    override var botRequireCodeGrant: Boolean = json["bot_require_code_grant"].asBoolean()

    override var botTermsOfService: URL? =
        if (json.hasNonNull("terms_of_service_url")) URL(json["terms_of_service_url"].asText())
        else null

    override var botPrivacyPolicy: URL? =
        if (json.hasNonNull("privacy_policy_url")) URL(json["privacy_policy_url"].asText())
        else null

    override var botOwner: User? =
        if (json.hasNonNull("owner")) UserImpl(json["owner"], json["owner"].get("id").asLong(), yde)
        else null

    override var verifyKey: String? =
        if (json.hasNonNull("verify_key")) json["verify_key"].asText() else null

    override var guild: Guild? =
        if (json.hasNonNull("guild_id")) yde.getGuildById(json["guild_id"].asLong()) else null

    override var gameSdkId: GetterSnowFlake? =
        if (json.hasNonNull("game_sdk_id")) GetterSnowFlake.of(json["game_sdk_id"].asLong())
        else null

    override var slug: String? = if (json.hasNonNull("slug")) json["slug"].asText() else null

    override var coverImage: String? =
        if (json.hasNonNull("cover_image")) json["cover_image"].asText() else null

    override var flags: Int? = if (json.hasNonNull("flags")) json["flags"].asInt() else null

    override var tags: Array<String>? =
        if (json.hasNonNull("tags")) json["tags"].asText().split(",").toTypedArray() else null

    override var name: String = json["name"].asText()

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).name(this.name).toString()
    }
}
