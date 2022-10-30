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

import io.github.ydwk.ydwk.util.Checks
import java.util.regex.Pattern

enum class Activity(val activity: Int) {
    /** Playing {name} e.g. Playing Minecraft */
    PLAYING(0),

    /** Streaming {details} e.g. Streaming Minecraft on Twitch */
    STREAMING(1),

    /** Listening to {name} e.g. Listening to Spotify. */
    LISTENING(2),

    /** Watching {name} e.g. Watching lfc tv. */
    WATCHING(3),

    /** {emoji} {name} e.g. :smiley: LFC are the best. */
    CUSTOM(4),

    /** Competing in {name} e.g. Competing in the world cup. */
    COMPETING(5),

    /** For future use or not implemented yet. */
    UNKNOWN(-1);

    private val allowedStreamingUrls: Pattern =
        Pattern.compile(
            "https?://(www\\.)?(twitch\\.tv/|youtube\\.com/watch\\?v=).+", Pattern.CASE_INSENSITIVE)

    var activityName: String? = null
    var url: String? = null

    fun watching(name: String, url: String): Int {
        Checks.checkLength(name, 128, "name")
        this.activityName = name
        Checks.checkUrl(url, allowedStreamingUrls)
        this.url = url
        return activity
    }

    fun playing(name: String): Int {
        Checks.checkLength(name, 128, "name")
        this.activityName = name
        return activity
    }

    fun listening(name: String): Int {
        Checks.checkLength(name, 128, "name")
        this.activityName = name
        return activity
    }

    fun streaming(name: String, url: String): Int {
        Checks.checkLength(name, 128, "name")
        this.activityName = name
        Checks.checkUrl(url, allowedStreamingUrls)
        this.url = url
        return activity
    }

    companion object {
        /**
         * Gets the activity from the provided id.
         *
         * @param id The id of the activity.
         * @return The activity.
         */
        fun fromId(id: Int): Activity {
            for (activity in values()) {
                if (activity.activity == id) {
                    return activity
                }
            }
            return UNKNOWN
        }
    }
}
