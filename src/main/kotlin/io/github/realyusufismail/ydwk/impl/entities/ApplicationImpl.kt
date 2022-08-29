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

    override val icon: String
        get() = json.get("icon").asText()

    override val description: String
        get() = json.get("description").asText()

    override val rpcOrigins: Array<String>
        get() = json.get("rpc_origins").map { it.asText() }.toTypedArray()

    override val botPublic: Boolean
        get() = json.get("bot_public").asBoolean()

    override val botRequireCodeGrant: Boolean
        get() = json.get("bot_require_code_grant").asBoolean()

    override val botTermsOfService: URL
        get() = URL(json.get("terms_of_service_url").asText())

    override val botPrivacyPolicy: URL
        get() = URL(json.get("privacy_policy_url").asText())

    override val botOwner: User
        get() = UserImpl(json.get("owner"), id, ydwk)

    override val verifyKey: String
        get() = json.get("verify_key").asText()

    override val guildId: GetterSnowFlake
        get() = GetterSnowFlake.of(json.get("guild_id").asLong())

    override val gameSdkId: GetterSnowFlake
        get() = GetterSnowFlake.of(json.get("primary_sku_id").asLong())

    override val slug: String
        get() = json.get("slug").asText()

    override val coverImage: String
        get() = json.get("cover_image").asText()

    override fun getIdLong(): Long {
        return id
    }

    override fun getName(): String {
        return json.get("name").asText()
    }
}
