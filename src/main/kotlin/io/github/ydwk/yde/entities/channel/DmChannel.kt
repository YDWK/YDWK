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
package io.github.ydwk.yde.entities.channel

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.util.GetterSnowFlake
import kotlinx.coroutines.CompletableDeferred

interface DmChannel : TextChannel {
    /**
     * The channel's last message id.
     *
     * @return the channel's last message id.
     */
    var lastMessageId: GetterSnowFlake?

    /**
     * The recipient of the dm.
     *
     * @return the recipient of the dm.
     */
    var recipient: User?

    /**
     * Retrieves the recipient of the dm.
     *
     * @return the recipient of the dm.
     */
    val retrieveRecipient: CompletableDeferred<User>
        get() = yde.restAPIMethodGetters.getUserRestAPIMethods().requestUser(idAsLong)
}
