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
package io.github.ydwk.yde.entities.message

import io.github.ydwk.yde.entities.util.GenericEntity
import io.github.ydwk.yde.util.NameAbleEntity
import io.github.ydwk.yde.util.SnowFlake
import java.net.URL

interface Attachment : SnowFlake, NameAbleEntity, GenericEntity {
    /**
     * The description of this attachment.
     *
     * @return The description of this attachment.
     */
    val description: String?

    /**
     * The media type of this attachment.
     *
     * @return The media type of this attachment.
     */
    val mediaType: String?

    /**
     * The url of this attachment.
     *
     * @return The url of this attachment.
     */
    val url: URL

    /**
     * The proxy url of this attachment.
     *
     * @return The proxy url of this attachment.
     */
    val proxyUrl: URL

    /**
     * The size of this attachment.
     *
     * @return The size of this attachment.
     */
    val size: Int

    /**
     * The height of this attachment.
     *
     * @return The height of this attachment.
     */
    val height: Int?

    /**
     * The width of this attachment.
     *
     * @return The width of this attachment.
     */
    val width: Int?

    /**
     * Gets weather this attachment is ephemeral.
     *
     * @return Weather this attachment is ephemeral.
     */
    val ephemeral: Boolean
}
