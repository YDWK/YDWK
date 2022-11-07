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
package io.github.ydwk.ydwk.impl.entities.audit

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.entities.audit.AuditLogChange
import io.github.ydwk.ydwk.entities.audit.AuditLogEntry
import io.github.ydwk.ydwk.entities.audit.AuditLogType
import io.github.ydwk.ydwk.util.EntityToStringBuilder

class AuditLogEntryImpl(
    override val ydwk: YDWK,
    override val json: JsonNode,
    override val idAsLong: Long
) : AuditLogEntry {
    override val targetId: String?
        get() = if (json.has("target_id")) json["target_id"].asText() else null

    override val changes: List<AuditLogChange>
        get() = json["changes"].map { AuditLogChangeImpl(ydwk, it) }

    override val user: User?
        get() = if (json.has("user_id")) ydwk.getUserById(json["user_id"].asLong()) else null

    override val type: AuditLogType
        get() = AuditLogType.fromType(json["action_type"].asInt())

    override val reason: String?
        get() = if (json.has("reason")) json["reason"].asText() else null

    override fun toString(): String {
        return EntityToStringBuilder(ydwk, this).toString()
    }
}
