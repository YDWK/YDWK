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
import io.github.realyusufismail.ydwk.ws.util.formatInstant
import java.time.Instant

class LoggedInImpl(
    override val loggedIn: Boolean,
) : LoggedIn {
    private var loggedInInstant: Instant? = null
    private var disconnectedInstant: Instant? = null

    override var loggedInTime: String? = null
        get() {
            if (loggedInInstant != null) {
                return formatInstant(loggedInInstant!!)
            }
            return null
        }

    override var disconnectionTime: String? = null
        get() {
            if (disconnectedInstant != null) {
                return formatInstant(disconnectedInstant!!)
            }
            return null
        }

    /** Used to set the logged in time */
    fun setLoggedInTime(): LoggedIn {
        loggedInInstant = Instant.now()
        return this
    }

    /** Used to set the disconnected time */
    fun setDisconnectedTime(): LoggedIn {
        disconnectedInstant = Instant.now()
        return this
    }
}
