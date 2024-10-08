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
package io.github.ydwk.yde.impl.entities.guild.schedule

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.guild.Member
import io.github.ydwk.yde.entities.guild.schedule.EntityMetadata
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl
import io.github.ydwk.yde.util.GetterSnowFlake

internal class EntityMetadataImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val scheduledEventId: GetterSnowFlake,
    override val user: User,
    override val member: Member?
) : EntityMetadata, ToStringEntityImpl<EntityMetadata>(yde, EntityMetadata::class.java)
