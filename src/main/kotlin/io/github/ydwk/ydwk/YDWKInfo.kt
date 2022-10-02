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

enum class YDWKInfo(val url: String) {
    DISCORD_GATEWAY_URL("wss://gateway.discord.gg/"),
    DISCORD_GATEWAY_VERSION("?v=10"),
    JSON_ENCODING("&encoding=json"),
    DISCORD_REST_URL("https://discord.com/api"),
    DISCORD_REST_VERSION("/v10"),
    FULL_DISCORD_REST_URL(DISCORD_REST_URL.url + DISCORD_REST_VERSION.url),
    GITHUB_URL("https://github.com/RealYusufIsmail/YDWK"),
    YDWK_VERSION("0.0.1");

    override fun toString(): String {
        return url
    }

    companion object {
        fun get(info: YDWKInfo): String {
            return info.toString()
        }
    }
}
