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

import io.github.ydwk.ydwk.entities.User
import io.github.ydwk.ydwk.util.SnowFlake

interface AuditLogEntry : SnowFlake {
    /**
     * Gets the targetId of this audit log entry.
     *
     * @return The targetId of this audit log entry.
     */
    val targetId: String?

    /**
     * Gets the changes of this audit log entry.
     *
     * @return The changes of this audit log entry.
     */
    val changes: List<AuditLogChange>

    /**
     * Gets the user who made this audit log entry.
     *
     * @return The user who made this audit log entry.
     */
    val user: User?

    /**
     * Gets the type of this audit log entry.
     *
     * @return The type of this audit log entry.
     */
    val type: AuditLogType

    /**
     * Gets the reason of this audit log entry.
     *
     * @return The reason of this audit log entry.
     */
    val reason: String?
}
