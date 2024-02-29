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
package io.github.ydwk.yde.impl.entities

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Application
import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.util.GetterSnowFlake
import java.net.URL

open class ApplicationImpl(
    override val json: JsonNode,
    override val idAsLong: Long,
    override val yde: YDE,
    override var icon: String?,
    override var description: String,
    override var rpcOrigins: Array<String>?,
    override var botPublic: Boolean,
    override var botRequireCodeGrant: Boolean,
    override var botTermsOfService: URL?,
    override var botPrivacyPolicy: URL?,
    override var botOwner: User?,
    override var verifyKey: String?,
    override var guild: Guild?,
    override var gameSdkId: GetterSnowFlake?,
    override var slug: String?,
    override var coverImage: String?,
    override var flags: Int?,
    override var tags: Array<String>?,
    override var name: String,
) : Application, ToStringEntityImpl<Application>(yde, Application::class.java)
