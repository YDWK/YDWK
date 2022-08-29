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
package io.github.realyusufismail.ydwk.entities

import io.github.realyusufismail.ydwk.entities.util.GenericEntity
import io.github.realyusufismail.ydwk.util.GetterSnowFlake
import io.github.realyusufismail.ydwk.util.NameAbleEntity
import io.github.realyusufismail.ydwk.util.SnowFlake
import java.net.URL

interface Application : SnowFlake, NameAbleEntity, GenericEntity {
    /** The icon hash of the application. */
    val icon: String?

    /** The description of the application. */
    val description: String

    /** An array of rpc origin urls, if rpc is enabled. */
    val rpcOrigins: Array<String>?

    /** If the bot can only be added by the bot owner. */
    val botPublic: Boolean

    /** If the bot will only join upon completion of the full oauth2 code grant flow */
    val botRequireCodeGrant: Boolean

    /** The bots terms of service. */
    val botTermsOfService: URL?

    /** The bots privacy policy. */
    val botPrivacyPolicy: URL?

    /** The owner of the bot. */
    val botOwner: User?

    /** The hex encoded key for verification in interactions and the GameSDK's GetTicket */
    val verifyKey: String?

    /** The id of the guild this application is for. */
    val guildId: GetterSnowFlake?

    /** The game sdk id of the application. */
    val gameSdkId: GetterSnowFlake?

    /** The url of the slug for the application. */
    val slug: String?

    /** The cover image hash. */
    val coverImage: String?
}
