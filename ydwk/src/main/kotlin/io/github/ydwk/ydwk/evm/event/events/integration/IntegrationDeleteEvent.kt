/*
 * Copyright 2024-2025 YDWK inc.
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
package io.github.ydwk.ydwk.evm.event.events.integration

import io.github.ydwk.yde.entities.Guild
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.evm.annotations.GuildEvent
import io.github.ydwk.ydwk.evm.event.Event

/**
 * This event is triggered when an integration is deleted.
 *
 * @param ydwk The [YDWK] instance.
 * @param guild The [Guild] the integration was removed from.
 * @param integrationId The id of the deleted integration.
 * @param applicationId The id of the bot/OAuth2 application (null if not applicable).
 */
@GuildEvent
data class IntegrationDeleteEvent(
    override val ydwk: YDWK,
    val guild: Guild,
    val integrationId: GetterSnowFlake,
    val applicationId: GetterSnowFlake?,
) : Event(ydwk)
