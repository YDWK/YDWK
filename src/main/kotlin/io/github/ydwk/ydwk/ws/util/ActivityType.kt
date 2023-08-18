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
package io.github.ydwk.ydwk.ws.util

enum class ActivityType(private val activity: Int) {
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

    companion object {

        /**
         * The activity from the provided id.
         *
         * @param id The id of the activity.
         * @return The activity.
         */
        fun fromInt(id: Int): ActivityType {
            for (activity in entries) {
                if (activity.activity == id) {
                    return activity
                }
            }
            return UNKNOWN
        }
    }

    /**
     * The id of the activity.
     *
     * @return The id of the activity.
     */
    fun getActivity(): Int {
        return activity
    }
}
