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
package io.github.ydwk.yde.entities.guild

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.entities.PermissionEntity
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.entities.channel.DmChannel
import io.github.ydwk.yde.entities.message.SendAble
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.rest.result.NoResult
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import kotlinx.coroutines.CompletableDeferred

/** This class is used to represent a discord guild member entity. */
interface Member : NameAbleEntity, GenericEntity, SendAble, SnowFlake, PermissionEntity {

    /**
     * Gets thw member's Guild.
     *
     * @return The member's Guild.
     */
    val guild: Guild

    /**
     * The user this guild member represents.
     *
     * @return The user this guild member represents.
     */
    var user: User

    /**
     * Gets this member guild nickname.
     *
     * @return This member guild nickname.
     */
    var nick: String?

    /**
     * The member's guild avatar hash.
     *
     * @return The member's guild avatar hash.
     */
    var guildAvatarHash: String?

    /**
     * The member's guild avatar.
     *
     * @return The member's guild avatar.
     */
    val guildAvatar: Avatar?

    /**
     * The ids of the roles this member is assigned.
     *
     * @return The ids of the roles this member is assigned.
     */
    val roleIds: List<GetterSnowFlake>

    /**
     * The roles of this member.
     *
     * @return The roles of this member.
     */
    val roles: List<Role?>

    /**
     * The time this member joined the guild.
     *
     * @return The time this member joined the guild.
     */
    var joinedAt: String?

    /**
     * The date the member started boosting the guild.
     *
     * @return The date the member started boosting the guild.
     */
    var premiumSince: String?

    /**
     * Whether the member is deafened in voice channels.
     *
     * @return Whether the member is deafened in voice channels.
     */
    var deaf: Boolean

    /**
     * Whether the member is muted in voice channels.
     *
     * @return Whether the member is muted in voice channels.
     */
    var mute: Boolean

    /**
     * Whether the user has not yet passed the guild's Membership Screening requirements.
     *
     * @return Whether the user has not yet passed the guild's Membership Screening requirements.
     */
    var pending: Boolean

    /**
     * If the member is timed out, then this is the time at which the timeout will end.
     *
     * @return If the member is timed out, then this is the time at which the timeout will end.
     */
    var timedOutUntil: String?

    /**
     * Weather this member is timed out.
     *
     * @return Weather this member is timed out.
     */
    val isTimedOut: Boolean
        get() = timedOutUntil != null

    /**
     * Whether the member is the owner of the guild.
     *
     * @return Whether the member is the owner of the guild.
     */
    val isOwner: Boolean

    /**
     * Creates a direct message channel with this member.
     *
     * @return A future that completes with the created channel.
     */
    val createDmChannel: CompletableDeferred<DmChannel>
        get() = user.createDmChannel

    /**
     * If the member is in avc it will get there voice state.
     *
     * @return The voice state of the member.
     */
    var voiceState: VoiceState?

    /**
     * Adds a role to this member.
     *
     * @param role The role to add.
     * @return A future that completes with an empty result.
     */
    fun addRole(role: Role): CompletableDeferred<NoResult> =
        yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .removeRoleFromMember(guild.idAsLong, user.idAsLong, role.idAsLong)

    /**
     * Adds a list of roles to this member.
     *
     * @param roles The roles to add.
     * @return A future that completes with an empty result.
     */
    fun addRoles(roles: List<Role>): List<CompletableDeferred<NoResult>> =
        yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .addRolesToMember(guild.idAsLong, user.idAsLong, roles.map { it.idAsLong })

    /**
     * Adds a list of roles to this member.
     *
     * @param roles The roles to add.
     * @return A future that completes with an empty result.
     */
    fun addRoles(vararg roles: Role): List<CompletableDeferred<NoResult>> = addRoles(roles.toList())

    /**
     * Removes a role from this member.
     *
     * @param role The role to remove.
     * @return A future that completes with an empty result.
     */
    fun removeRole(role: Role): CompletableDeferred<NoResult> =
        yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .removeRoleFromMember(guild.idAsLong, user.idAsLong, role.idAsLong)

    /**
     * Removes a list of roles from this member.
     *
     * @param roles The roles to remove.
     * @return A future that completes an empty result.
     */
    fun removeRoles(roles: List<Role>): List<CompletableDeferred<NoResult>> =
        yde.restAPIMethodGetters
            .getMemberRestAPIMethods()
            .removeRolesFromMember(guild.idAsLong, user.idAsLong, roles.map { it.idAsLong })

    /**
     * Removes a list of roles from this member.
     *
     * @param roles The roles to remove.
     * @return A future that completes with an empty result.
     */
    fun removeRoles(vararg roles: Role): List<CompletableDeferred<NoResult>> =
        removeRoles(roles.toList())
}
