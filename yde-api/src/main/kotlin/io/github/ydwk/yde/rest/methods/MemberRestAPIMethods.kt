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
package io.github.ydwk.yde.rest.methods

import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.result.NoResult

interface MemberRestAPIMethods {

    /**
     * Adds a role to a member.
     *
     * @param guildId The guild id.
     * @param userId The user id.
     * @param roleId The role id.
     * @return A [RestResult] that will contain [NoResult].
     */
    suspend fun addRoleToMember(guildId: Long, userId: Long, roleId: Long): RestResult<NoResult>

    /**
     * Adds a list of roles to a member.
     *
     * @param guildId The guild id.
     * @param userId The user id.
     * @param roleIds The role ids.
     * @return A list of [RestResult]s that will contain [NoResult].
     */
    suspend fun addRolesToMember(
        guildId: Long,
        userId: Long,
        roleIds: List<Long>
    ): List<RestResult<NoResult>>

    /**
     * Removes a role from a member.
     *
     * @param guildId The guild id.
     * @param userId The user id.
     * @param roleId The role id.
     * @return A [RestResult] that will contain [NoResult
     */
    suspend fun removeRoleFromMember(
        guildId: Long,
        userId: Long,
        roleId: Long
    ): RestResult<NoResult>

    /**
     * Removes a list of roles from a member.
     *
     * @param guildId The guild id.
     * @param userId The user id.
     * @param roleIds The role ids.
     * @return A list of [RestResult]s that will contain [NoResult].
     */
    suspend fun removeRolesFromMember(
        guildId: Long,
        userId: Long,
        roleIds: List<Long>
    ): List<RestResult<NoResult>>
}
