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
package io.github.realyusufismail.ydwk.ws.util.impl

import io.github.realyusufismail.ydwk.ws.util.LoggedIn
import java.time.Duration
import java.time.Instant

class LoggedInImpl(
    override val loggedIn: Boolean,
    override var loggedInTime: Duration?,
    override var disconnectionTime: Duration?
) : LoggedIn {

    override fun subscribe(function: (LoggedIn) -> Unit) {
        // if logged in then call function and return the logged in time
        when {
            loggedIn -> {
                function(this)
                return
            }
            loggedInTime != null -> {
                // if logged in time is not null then calculate the duration between now and the
                // logged in time
                val duration = Duration.between(Instant.now(), loggedInTime!!.toInstant())
                // if the duration is greater than the threshold then call the function
                if (duration.toMinutes() > THRESHOLD) {
                    function(this)
                }
            }
            else -> {
                // if not logged in then return null
                return
            }
        }
    }

    companion object {
        const val THRESHOLD = 5

        // change duration to Instant
        fun Duration.toInstant(): Instant {
            return Instant.ofEpochMilli(this.toMillis())
        }
    }
}
