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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.message.SendAble
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import java.awt.Color
import kotlinx.coroutines.CompletableDeferred

interface User : SnowFlake, GenericEntity, NameAbleEntity, SendAble {
    /**
     * The user's 4-digit discord-tag.
     *
     * @return the user's 4-digit discord-tag.
     */
    @Deprecated("Discord has removed discriminator in favour of the new @mention system.")
    var discriminator: String

    /**
     * The global name of the user.
     *
     * @return the global name of the user.
     */
    var globalName: String

    /**
     * The user's avatar hash.
     *
     * @return the user's avatar hash.
     */
    var avatarHash: String?

    /**
     * The user's guild specific avatar hash.
     *
     * @param guildId the guild id.
     * @return the user's guild specific avatar hash.
     */
    fun guildAvatarHash(guildId: Long): String?

    /**
     * The user's avatar.
     *
     * @return the user's avatar.
     */
    val avatar: Avatar

    /**
     * The user's guild specific avatar.
     *
     * @param guildId the guild id.
     * @return the user's guild specific avatar.
     */
    fun guildAvatar(guildId: Long): Avatar?

    /**
     * Whether the user has a default avatar.
     *
     * @return whether the user has a default avatar.
     */
    val hasDefaultAvatar: Boolean

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
    val createDmChannel: CompletableDeferred<DmChannel>
        get() = yde.restAPIMethodGetters.getUserRestAPIMethods().createDm(idAsLong)
}
