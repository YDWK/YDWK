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
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.ydwk

import io.github.ydwk.yde.util.Checks
import io.github.ydwk.ydwk.ws.util.ActivityType
import java.util.regex.Pattern

object Activity {

    /**
     * Sets the activity of the bot to watching {name}.
     *
     * @param name The name of the thing the bot is watching.
     * @param url What the bot is watching.
     * @param state User's current party status, or text used for a custom status
     * @return The activity payload.
     */
    fun watching(name: String, url: String, state: String?): ActivityPayload {
        return ActivityPayload(name, ActivityType.WATCHING.getActivity(), url, state)
    }

    /**
     * Sets the activity of the bot to playing {name}.
     *
     * @param name The name of the thing the bot is playing.
     * @param state User's current party status, or text used for a custom status
     * @return The activity payload.
     */
    fun playing(name: String, state: String?): ActivityPayload {
        return ActivityPayload(name, ActivityType.PLAYING.getActivity(), null, state)
    }

    /**
     * Sets the activity of the bot to listening to {name}.
     *
     * @param name The name of the thing the bot is listening to.
     * @param state User's current party status, or text used for a custom status
     * @return The activity payload.
     */
    fun listening(name: String, state: String?): ActivityPayload {
        return ActivityPayload(name, ActivityType.LISTENING.getActivity(), null, state)
    }

    /**
     * Sets the activity of the bot to competing in {name}.
     *
     * @param name The name of the thing the bot is competing in.
     * @param state User's current party status, or text used for a custom status
     * @return The activity payload.
     */
    fun streaming(name: String, url: String, state: String?): ActivityPayload {
        return ActivityPayload(name, ActivityType.STREAMING.getActivity(), url, state)
    }
}

/**
 * Returns a new [ActivityPayload] with the given [name] and [type].
 *
 * @param name The name of the activity.
 * @param type The type of the activity.
 * @param url The url of the activity.
 * @param state User's current party status, or text used for a custom status
 */
data class ActivityPayload(
    val name: String,
    val type: Int,
    val url: String? = null,
    val state: String? = null
) {
    private val allowedStreamingUrls: Pattern =
        Pattern.compile(
            "https?://(www\\.)?(twitch\\.tv/|youtube\\.com/watch\\?v=).+", Pattern.CASE_INSENSITIVE)

    init {
        name.let { // Use let to safely work with non-null name
            Checks.checkLength(it, 128, "name")
        }

        state?.let { // Use let to safely work with non-null state
            Checks.checkLength(it, 128, "state")
        }

        url?.let { // Use let to safely work with non-null url
            Checks.checkLength(it, 512, "url")
        }
    }
}
