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
package io.github.ydwk.yde.entities.audit

import io.github.ydwk.yde.entities.User
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.SnowFlake

interface AuditLogEntry : SnowFlake, GenericEntity {
    /**
     * The targetId of this audit log entry.
     *
     * @return The targetId of this audit log entry.
     */
    val targetId: String?

    /**
     * The changes of this audit log entry.
     *
     * @return The changes of this audit log entry.
     */
    val changes: List<AuditLogChange>

    /**
     * The user who made this audit log entry.
     *
     * @return The user who made this audit log entry.
     */
    val user: User?

    /**
     * The type of this audit log entry.
     *
     * @return The type of this audit log entry.
     */
    val type: AuditLogType

    /**
     * The reason of this audit log entry.
     *
     * @return The reason of this audit log entry.
     */
    val reason: String?
}
