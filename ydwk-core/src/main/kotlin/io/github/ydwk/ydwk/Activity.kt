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
    private val allowedStreamingUrls: Pattern =
        Pattern.compile(
            "https?://(www\\.)?(twitch\\.tv/|youtube\\.com/watch\\?v=).+", Pattern.CASE_INSENSITIVE)

    fun watching(name: String, url: String): ActivityPayload {
        Checks.checkLength(name, 128, "name")
        Checks.checkUrl(url, allowedStreamingUrls)
        return ActivityPayload(name, ActivityType.WATCHING.getActivity(), url)
    }

    fun playing(name: String): ActivityPayload {
        Checks.checkLength(name, 128, "name")
        return ActivityPayload(name, ActivityType.PLAYING.getActivity())
    }

    fun listening(name: String): ActivityPayload {
        Checks.checkLength(name, 128, "name")
        return ActivityPayload(name, ActivityType.LISTENING.getActivity())
    }

    fun streaming(name: String, url: String): ActivityPayload {
        Checks.checkLength(name, 128, "name")
        Checks.checkUrl(url, allowedStreamingUrls)
        return ActivityPayload(name, ActivityType.STREAMING.getActivity(), url)
    }
}

/** Returned when setting an activity. */
data class ActivityPayload(val name: String, val type: Int, val url: String? = null)
