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
package io.github.ydwk.ydwk

enum class YDWKInfo(private val url: String) {
    DISCORD_GATEWAY_URL("wss://gateway.discord.gg/"),
    DISCORD_GATEWAY_VERSION("?v=10"),
    JSON_ENCODING("&encoding=json"),
    DISCORD_REST_URL("https://discord.com/api"),
    DISCORD_REST_VERSION("/v10"),
    FULL_DISCORD_REST_URL(DISCORD_REST_URL.url + DISCORD_REST_VERSION.url),
    VOICE_GATEWAY_VERSION("/?v=4"),
    GITHUB_URL("https://github.com/RealYusufIsmail/YDWK"),
    YDWK_VERSION("0.0.7");

    companion object {
        /**
         * Get the full discord gateway url.
         *
         * @param info The [YDWKInfo] to get the full url from.
         * @return The full discord gateway url.
         */
        fun fromString(info: YDWKInfo): String {
            return info.toString()
        }
    }

    /**
     * Gets the url of the [YDWKInfo].
     *
     * @return The url of the [YDWKInfo].
     */
    fun getUrl(): String {
        return url
    }
}
