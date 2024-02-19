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
package io.github.ydwk.yde.entities

import io.github.ydwk.yde.entities.audit.AuditLogEntry
import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.interaction.application.type.SlashCommand

interface AuditLog : GenericEntity {
    /**
     * Gets a list of application commands referenced in the audit log.
     *
     * @return A list of application commands referenced in the audit log.
     */
    val applicationCommands: List<SlashCommand>

    /**
     * Gets a list of audit log entries, sorted from most to least recent.
     *
     * @return A list of audit log entries, sorted from most to least recent.
     */
    val entries: List<AuditLogEntry>
}
