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
package io.github.ydwk.yde.impl.rest.methods

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.methods.MemberRestAPIMethods
import io.github.ydwk.yde.rest.result.NoResult

class MemberRestAPIMethodsImpl(val yde: YDE) : MemberRestAPIMethods {
    override suspend fun addRoleToMember(
        guildId: Long,
        userId: Long,
        roleId: Long
    ): RestResult<NoResult> {
        return yde.restApiManager
            .put(
                null,
                EndPoint.GuildEndpoint.ADD_ROLE,
                guildId.toString(),
                userId.toString(),
                roleId.toString())
            .executeWithNoResult()
    }

    override suspend fun addRolesToMember(
        guildId: Long,
        userId: Long,
        roleIds: List<Long>
    ): List<RestResult<NoResult>> {
        val actions = mutableListOf<RestResult<NoResult>>()
        for (roleId in roleIds) {
            actions.add(addRoleToMember(guildId, userId, roleId))
        }
        return actions
    }

    override suspend fun removeRoleFromMember(
        guildId: Long,
        userId: Long,
        roleId: Long
    ): RestResult<NoResult> {
        return yde.restApiManager
            .delete(
                null,
                EndPoint.GuildEndpoint.REMOVE_ROLE,
                guildId.toString(),
                userId.toString(),
                roleId.toString())
            .executeWithNoResult()
    }

    override suspend fun removeRolesFromMember(
        guildId: Long,
        userId: Long,
        roleIds: List<Long>
    ): List<RestResult<NoResult>> {
        val actions = mutableListOf<RestResult<NoResult>>()
        for (roleId in roleIds) {
            actions.add(removeRoleFromMember(guildId, userId, roleId))
        }
        return actions
    }
}
