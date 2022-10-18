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
package io.github.ydwk.ydwk.entities.guild

import io.github.ydwk.ydwk.entities.Guild
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.guild.enums.MemberPermission
import io.github.ydwk.ydwk.entities.message.Sendeadble
import io.github.ydwk.ydwk.entities.util.GenericEntity
import io.github.ydwk.ydwk.util.GetterSnowFlake
import io.github.ydwk.ydwk.util.NameAbleEntity
import io.github.ydwk.ydwk.util.SnowFlake
import java.util.*

/** This class is used to represent a discord guild member entity. */
interface Member : NameAbleEntity, GenericEntity, Sendeadble, SnowFlake {

    /** Used to get thw member's Guild */
    val guild: Guild

    /** The user this guild member represents. */
    var user: User

    /** This users guild nickname. */
    var nick: String?

    /** The user's avatar hash. */
    var avatar: String?

    /**
     * Gets the ids of the roles this member is assigned.
     *
     * @return The ids of the roles this member is assigned.
     */
    val roleIds: List<GetterSnowFlake>

    /**
     * Gets the roles of this member.
     *
     * @return The roles of this member.
     */
    val roles: List<Role?>

    /** The date the user joined the guild. */
    var joinedAt: String?

    /** The date the user started boosting the guild. */
    var premiumSince: String?

    /** Whether the user is deafened in voice channels. */
    var deaf: Boolean

    /** Whether the user is muted in voice channels. */
    var mute: Boolean

    /** Whether the user has not yet passed the guild's Membership Screening requirements. */
    var pending: Boolean

    /** Weather this user is timed out. */
    var timedOutUntil: String?

    /** Used to get the permissions of this member. */
    val permissions: EnumSet<MemberPermission>

    /**
     * Used to check if the member has a specific permission.
     *
     * @param permission The permission to check.
     * @return True if the member has the permission, false otherwise.
     */
    fun hasPermission(permission: MemberPermission): Boolean
}
