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
package io.github.ydwk.yde.entities.guild.invite

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.guild.Invite
import io.github.ydwk.yde.impl.entities.guild.InviteImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.util.Checks
import io.github.ydwk.yde.util.GetterSnowFlake
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CompletableDeferred
import okhttp3.RequestBody.Companion.toRequestBody

class InviteCreator(val yde: YDE, private val channelId: String) {
    private var maxAge = 86400
    private var maxUses = 0
    private var temporary = false
    private var unique = false
    private var targetType: TargetType? = null
    private var targetUserId: GetterSnowFlake? = null
    private var targetApplicationId: GetterSnowFlake? = null

    /**
     * Sets the max age of the invite. Between 0 and 604800 (7 days)
     *
     * @param maxAge the max age of the invite.
     * @return the invite creator.
     */
    fun maxAge(maxAge: Int): InviteCreator {
        Checks.customCheck(maxAge in 0..604800, "Max age must be between 0 and 604800")
        this.maxAge = maxAge
        return this
    }

    /**
     * Sets the max uses of the invite. Between 0 and 100
     *
     * @param maxUses the max uses of the invite.
     * @return the invite creator.
     */
    fun maxUses(maxUses: Int): InviteCreator {
        Checks.customCheck(maxUses in 0..100, "Max uses must be between 0 and 100")
        this.maxUses = maxUses
        return this
    }

    /**
     * Sets the temporary state of the invite.
     *
     * @param temporary the temporary state of the invite.
     * @return the invite creator.
     */
    fun temporary(temporary: Boolean): InviteCreator {
        this.temporary = temporary
        return this
    }

    /**
     * Sets the unique state of the invite.
     *
     * @param unique the unique state of the invite.
     * @return the invite creator.
     */
    fun unique(unique: Boolean): InviteCreator {
        this.unique = unique
        return this
    }

    /**
     * Sets the target type of the invite.
     *
     * @param targetType the target type of the invite.
     * @return the invite creator.
     */
    fun targetType(targetType: TargetType): InviteCreator {
        this.targetType = targetType
        return this
    }

    /**
     * Sets the target user of the invite.
     *
     * @param targetUserId the target user of the invite.
     * @return the invite creator.
     */
    fun targetUser(targetUserId: GetterSnowFlake): InviteCreator {
        this.targetUserId = targetUserId
        return this
    }

    /**
     * Sets the target application of the invite.
     *
     * @param targetApplicationId the target application of the invite.
     * @return the invite creator.
     */
    fun targetApplication(targetApplicationId: GetterSnowFlake): InviteCreator {
        this.targetApplicationId = targetApplicationId
        return this
    }

    /**
     * Creates the invite.
     *
     * @return The [CompletableFuture] of the [Invite].
     */
    fun create(): CompletableDeferred<InviteImpl> {
        val json = yde.objectMapper.createObjectNode()

        json.put("max_age", maxAge)
        json.put("max_uses", maxUses)
        json.put("temporary", temporary)
        json.put("unique", unique)

        if (targetType != null) {
            json.put("target_type", targetType!!.getValue())
        }

        if (targetUserId != null) {
            json.put("target_user_id", targetUserId!!.asLong)
        }

        if (targetApplicationId != null) {
            json.put("target_application_id", targetApplicationId!!.asLong)
        }

        return yde.restApiManager
            .post(
                json.toString().toRequestBody(), EndPoint.ChannelEndpoint.CREATE_INVITE, channelId)
            .execute {
                val jsonBody = it.jsonBody
                if (jsonBody == null) {
                    throw IllegalStateException("json body is null")
                } else {
                    InviteImpl(yde, jsonBody)
                }
            }
    }
}
