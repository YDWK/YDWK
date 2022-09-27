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
package io.github.realyusufismail.ydwk.rest

import io.github.realyusufismail.ydwk.YDWKInfo

open class EndPoint {
    interface IEnumEndpoint {
        /**
         * Gets the endpoint.
         *
         * @return the endpoint
         */
        fun getEndpoint(): String

        /**
         * Gets the endpoint with the rest api url.
         *
         * @return the endpoint with the rest api url
         */
        fun getFullEndpoint(): String {
            return YDWKInfo.FULL_DISCORD_REST_URL.url + getEndpoint()
        }

        /**
         * Gets the rest api url.
         *
         * @return the rest api url
         */
        fun getRestApiUrl(): String {
            return YDWKInfo.FULL_DISCORD_REST_URL.url
        }

        fun containsParam(): Boolean {
            return getEndpoint().contains("%s")
        }

        /**
         * Gets the full endpoint with the rest api url, including the parameters.
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
        CREATE_BAN("/guilds/%s/bans/%s");

        override fun getEndpoint(): String {
            return endPoint
        }
    }

    enum class UserEndpoint(val endPoint: String)
}