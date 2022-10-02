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

import io.github.realyusufismail.ydwk.entities.Guild
import io.github.realyusufismail.ydwk.entities.User
import io.github.realyusufismail.ydwk.entities.util.GenericEntity
import io.github.realyusufismail.ydwk.util.NameAbleEntity

/** This class is used to represent a discord guild member entity. */
interface Member : NameAbleEntity, GenericEntity {

    /** Used to get thw member's Guild */
    val guild: Guild

    /** The user this guild member represents. */
    var user: User?

    /** This users guild nickname. */
    var nick: String?

    /** The user's avatar hash. */
    var avatar: String?

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

    /**
     * Total permissions of the member in the channel, including overwrites, returned when in the
     * interaction object.
     */
    var permissions: String?

    /** Weather this user is timed out. */
    var timedOutUntil: String?
}
