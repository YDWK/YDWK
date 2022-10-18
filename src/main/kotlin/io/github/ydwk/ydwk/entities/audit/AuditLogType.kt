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
package io.github.ydwk.ydwk.entities.audit

import io.github.ydwk.ydwk.entities.Guild
import kotlin.reflect.KClass

enum class AuditLogType(private val type: Int, private val objectChanged: KClass<Guild>) {
    /** The guild settings were updated. */
    GUILD_UPDATE(1, Guild::class),
    ;

    companion object {
        /**
         * Gets the [AuditLogType] of the provided [type].
         *
         * @param type The type to get the [AuditLogType] of.
         * @return The [AuditLogType] of the provided [type].
         */
        fun fromType(type: Int): AuditLogType {
            return values().first { it.type == type }
        }
    }

    /**
     * Gets the type of this [AuditLogType].
     *
     * @return The type of this [AuditLogType].
     */
    fun getType(): Int {
        return type
    }
}
