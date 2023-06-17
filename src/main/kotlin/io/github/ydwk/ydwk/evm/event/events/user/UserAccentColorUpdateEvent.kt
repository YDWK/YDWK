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
package io.github.ydwk.ydwk.evm.event.events.user

import io.github.ydwk.yde.entities.User
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.UserEvent
import java.awt.Color

/**
 * This event is triggered when a user's accent color is updated.
 *
 * @param ydwk The [YDWK] instance.
 * @param entity The user whose avatar was updated.
 * @param oldAccentColor The user's old accent color.
 * @param newAccentColor The user's new accent color.
 */
@UserEvent
data class UserAccentColorUpdateEvent(
    override val ydwk: YDWK,
    override val entity: User,
    val oldAccentColor: Color?,
    val newAccentColor: Color?,
) : GenericUserUpdateEvent<Color?>(ydwk, entity, oldAccentColor, newAccentColor)
