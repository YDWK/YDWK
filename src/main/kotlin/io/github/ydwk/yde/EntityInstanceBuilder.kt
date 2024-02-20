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
package io.github.ydwk.yde

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.entities.*
import io.github.ydwk.yde.entities.guild.*

interface EntityInstanceBuilder {

    /**
     * Used to build an instance of [User]
     *
     * @param json the json
     * @return [User] the user
     */
    fun buildUser(json: JsonNode): User

    /**
     * Used to build an instance of [Bot]
     *
     * @param json the json
     * @return [Bot] the bot
     */
    fun buildBot(json: JsonNode): Bot

    /**
     * Used to build an instance of [Guild]
     *
     * @param json the json
     * @return [Guild] the guild
     */
    fun buildGuild(json: JsonNode): Guild

    /**
     * Used to build an instance of [Message]
     *
     * @param json the json
     * @return [Message] the message
     */
    fun buildMessage(json: JsonNode): Message

    /**
     * Used to build an instance of [Sticker]
     *
     * @param json the json
     * @return [Sticker] the sticker
     */
    fun buildSticker(json: JsonNode): Sticker

    /**
     * Used to build an instance of [UnavailableGuild]
     *
     * @param json the json
     * @return [UnavailableGuild] the unavailable guild
     */
    fun buildUnavailableGuild(json: JsonNode): UnavailableGuild

    /**
     * Used to build an instance of [VoiceState]
     *
     * @param json the json
     * @return [VoiceState] the voice state
     */
    fun buildVoiceState(json: JsonNode): VoiceState

    /**
     * Used to build an instance of [AuditLog]
     *
     * @param json the json
     * @return [AuditLog] the audit log
     */
    fun buildAuditLog(json: JsonNode): AuditLog

    /**
     * Used to build an instance of [Application]
     *
     * @param json the json
     * @return [Application] the application
     */
    fun buildApplication(json: JsonNode): Application

    // Guild entities

    /**
     * Used to build an instance of [Ban]
     *
     * @param json the json
     * @return [Ban] the ban
     */
    fun buildBan(json: JsonNode): Ban

    /**
     * Used to build an instance of [GuildScheduledEvent]
     *
     * @param json the json
     * @return [GuildScheduledEvent] the guild scheduled event
     */
    fun buildGuildScheduledEvent(json: JsonNode): GuildScheduledEvent

    /**
     * Used to build an instance of [Invite]
     *
     * @param json the json
     * @return [Invite] the invite
     */
    fun buildInvite(json: JsonNode): Invite

    /**
     * Used to build an instance of [Member]
     *
     * @param json the json
     * @param guild the guild
     * @param backUpUser the back up user
     * @return [Member] the member
     */
    fun buildMember(json: JsonNode, guild: Guild, backUpUser: User? = null): Member

    /**
     * Used to build an instance of [Role]
     *
     * @param json the json
     * @return [Role] the role
     */
    fun buildRole(json: JsonNode): Role

    /**
     * Used to build an instance of [WelcomeScreen]
     *
     * @param json the json
     * @return [WelcomeScreen] the welcome screen
     */
    fun buildWelcomeScreen(json: JsonNode): WelcomeScreen
}
