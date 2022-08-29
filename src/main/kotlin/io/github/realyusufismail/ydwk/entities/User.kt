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
import io.github.realyusufismail.ydwk.util.NameAbleEntity
import io.github.realyusufismail.ydwk.util.SnowFlake
import java.awt.Color

interface User : SnowFlake, GenericEntity, NameAbleEntity {

    /** The user's 4-digit discord-tag */
    val discriminator: String

    /** The user's avatar hash */
    val avatar: String?

    /** Whether the user belongs to an OAuth2 application */
    val bot: Boolean

    /** Whether the user is an Official Discord System user (part of the urgent message system) */
    val system: Boolean

    /** Whether the user has two factor enabled on their account */
    val mfaEnabled: Boolean

    /** The user's banner hash */
    val banner: String?

    /** The user's banner color encoded as an integer representation of hexadecimal color code */
    val accentColor: Color?

    /** The user's chosen language option */
    val locale: String?

    /** Whether the email on this account has been verified */
    val verified: Boolean?

    /** The flags on a user's account */
    val flags: Int?

    /** The type of Nitro subscription on a user's account */
    val premiumType: Int?

    /** The public flags on a user's account */
    val publicFlags: Int?
}