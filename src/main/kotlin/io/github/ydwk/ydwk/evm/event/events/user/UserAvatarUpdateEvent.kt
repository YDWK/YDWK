/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.ydwk.evm.event.events.user

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.user.Avatar
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.UserEvent

/**
 * This event is triggered when a user's avatar is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param entity The user whose avatar was updated.
 * @param oldAvatar The user's old avatar.
 * @param newAvatar The user's new avatar.
 */
@UserEvent
data class UserAvatarUpdateEvent(
    override val ydwk: YDWK,
    override val entity: User,
    val oldAvatar: Avatar?,
    val newAvatar: Avatar?,
) : GenericUserUpdateEvent<Avatar?>(ydwk, entity, oldAvatar, newAvatar)
