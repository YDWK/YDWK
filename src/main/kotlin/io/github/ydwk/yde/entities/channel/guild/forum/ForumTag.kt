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
package io.github.ydwk.yde.entities.channel.guild.forum

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.GetterSnowFlake
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake

interface ForumTag : SnowFlake, GenericEntity, NameAbleEntity {

    /**
     * Whether this tag can only be added to or removed from threads by a member with the
     * MANAGE_THREADS permission.
     *
     * @return whether this tag can only be added to or removed from threads by a member with the
     *   MANAGE_THREADS permission.
     */
    val isManaged: Boolean

    /**
     * The emoji id of this tag.
     *
     * @return the emoji id of this tag.
     */
    val emojiId: GetterSnowFlake?

    /**
     * The emoji name of this tag.
     *
     * @return the emoji name of this tag.
     */
    val emojiName: String?
}
