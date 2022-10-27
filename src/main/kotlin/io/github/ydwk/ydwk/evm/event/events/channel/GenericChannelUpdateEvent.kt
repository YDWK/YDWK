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
package io.github.ydwk.ydwk.evm.event.events.channel

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.channel.GuildChannel
import io.github.ydwk.ydwk.evm.backend.update.IEventUpdate

open class GenericChannelUpdateEvent<T>(
    override val ydwk: YDWK,
    override val entity: GuildChannel,
    override val oldValue: T,
    override val newValue: T
) : IEventUpdate<GuildChannel, T>