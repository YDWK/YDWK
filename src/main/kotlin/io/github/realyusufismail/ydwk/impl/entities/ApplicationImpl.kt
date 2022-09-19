/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.realyusufismail.ydwk.YDWK
import io.github.realyusufismail.ydwk.entities.Application
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.util.GetterSnowFlake
import java.net.URL

class ApplicationImpl(override val json: JsonNode, private val id: Long, override val ydwk: YDWK) :
    Application {

    override var icon: String? = json["icon"].asText()

    override var description: String = json["description"].asText()

    override var rpcOrigins: Array<String>? = json["rpc_origins"].map { it.asText() }.toTypedArray()

    override var botPublic: Boolean = json["bot_public"].asBoolean()

    override var botRequireCodeGrant: Boolean = json["bot_require_code_grant"].asBoolean()

    override var botTermsOfService: URL? = URL(json["terms_of_service_url"].asText())

    override var botPrivacyPolicy: URL? = URL(json["privacy_policy_url"].asText())

    override var botOwner: User? = UserImpl(json["owner"], json["owner"].get("id").asLong(), ydwk)

    override var verifyKey: String? = json["verify_key"].asText()

    override var guildId: GetterSnowFlake? = GetterSnowFlake.of(json["guild_id"].asLong())

    override var gameSdkId: GetterSnowFlake? = GetterSnowFlake.of(json.get("game_sdk_id").asLong())

    override var slug: String? = json["slug"].asText()

    override var coverImage: String? = json["cover_image"].asText()

    override fun getIdLong(): Long {
        return id
    }

    override var name: String = json["name"].asText()
}
