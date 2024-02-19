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
package io.github.ydwk.yde.impl.entities.audit

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.audit.AuditLogChange
import io.github.ydwk.yde.entities.audit.AuditLogEntry
import io.github.ydwk.yde.entities.audit.AuditLogType
import io.github.ydwk.yde.util.EntityToStringBuilder

class AuditLogEntryImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val idAsLong: Long,
) : AuditLogEntry {
    override val targetId: String?
        get() = if (json.has("target_id")) json["target_id"].asText() else null

    override val changes: List<AuditLogChange>
        get() = json["changes"].map { AuditLogChangeImpl(yde, it) }

    override val user: User?
        get() = if (json.has("user_id")) yde.getUserById(json["user_id"].asLong()) else null

    override val type: AuditLogType
        get() = AuditLogType.fromInt(json["action_type"].asInt())

    override val reason: String?
        get() = if (json.has("reason")) json["reason"].asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(yde, this).toString()
    }
}
