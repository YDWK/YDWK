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
package io.github.ydwk.yde.rest

import io.github.ydwk.yde.rest.RestApiManager.Companion.FULL_DISCORD_REST_URL

open class EndPoint {
    interface IEnumEndpoint {
        /**
         * The endpoint.
         *
         * @return the endpoint
         */
        fun getEndpoint(): String

        /**
         * The endpoint with the rest api url.
         *
         * @return the endpoint with the rest api url
         */
        fun getFullEndpoint(): String {
            return FULL_DISCORD_REST_URL + getEndpoint()
        }

        /**
         * The rest api url.
         *
         * @return the rest api url
         */
        fun getRestApiUrl(): String {
            return FULL_DISCORD_REST_URL
        }

        fun containsParam(): Boolean {
            return getEndpoint().contains("%s")
        }

        /**
         * The full endpoint with the rest api url, including the parameters.
         *
         * @param params the parameters
         * @return the full endpoint with the rest api url, including the parameters
         */
        // vararg = variable number of arguments
        fun getFullEndpointWithParams(vararg params: String): String {
            val endpoint = getEndpoint()
            val sb = StringBuilder(endpoint)
            sb.insert(0, getRestApiUrl())
            params.forEach { param -> sb.replace(sb.indexOf("%s"), sb.indexOf("%s") + 2, param) }
            return sb.toString()
        }
    }

    enum class GuildEndpoint(private val endPoint: String) : IEnumEndpoint {
        GET_BANS("/guilds/%s/bans"),
        BAN("/guilds/%s/bans/%s"),
        KICK("/guilds/%s/members/%s"),
        GET_AUDIT_LOGS("/guilds/%s/audit-logs"),
        GET_MEMBERS("/guilds/%s/members"),
        GET_GUILD("/guilds/%s"),
        GET_GUILDS("/guilds"),
        GET_GUILD_CHANNELS("/guilds/%s/channels"),
        GET_MEMBER("/guilds/%s/members/%s"),
        CREATE_GUILD("/guilds"),
        CREATE_ROLE("/guilds/%s/roles"),
        CREATE_CHANNEL("/guilds/%s/channels"),
        ADD_ROLE("/guilds/%s/members/%s/roles/%s"),
        REMOVE_ROLE("/guilds/%s/members/%s/roles/%s");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class UserEndpoint(private val endPoint: String) : IEnumEndpoint {
        CREATE_DM("/users/@me/channels"),
        GET_USER("/users/%s"),
        GET_USERS("/users");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class ApplicationCommandsEndpoint(private val endPoint: String) : IEnumEndpoint {
        CREATE_GLOBAL_COMMAND("/applications/%s/commands"),
        GET_GLOBAL_COMMANDS("/applications/%s/commands"),
        CREATE_GUILD_COMMAND("/applications/%s/guilds/%s/commands"),
        GET_GUILD_COMMANDS("/applications/%s/guilds/%s/commands"),
        REPLY_TO_SLASH_COMMAND("/interactions/%s/%s/callback"),
        DELETE_GUILD_COMMAND("/applications/%s/guilds/%s/commands/%s"),
        DELETE_GLOBAL_COMMAND("/applications/%s/commands/%s");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class ChannelEndpoint(private val endPoint: String) : IEnumEndpoint {
        CREATE_MESSAGE("/channels/%s/messages"),
        GET_CHANNEL("/channels/%s"),
        CREATE_INVITE("/channels/%s/invites");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class VoiceEndpoint(private val endPoint: String) : IEnumEndpoint {
        GET_VOICE_REGIONS("/voice/regions");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class MessageEndpoint(private val endPoint: String) : IEnumEndpoint {
        DELETE_MESSAGE("/channels/%s/messages/%s");

        override fun getEndpoint(): String {
            return endPoint
        }
    }
}
