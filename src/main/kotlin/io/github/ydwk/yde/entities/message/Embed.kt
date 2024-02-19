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

import io.github.ydwk.yde.entities.message.embed.*
import io.github.ydwk.yde.entities.util.GenericEntity
import java.awt.Color
import java.net.URL

interface Embed : GenericEntity {
    /**
     * The title of this embed.
     *
     * @return The title of this embed.
     */
    val title: String?

    /**
     * The type of this embed.
     *
     * @return The type of this embed.
     */
    val type: EmbedType?

    /**
     * The description of this embed.
     *
     * @return The description of this embed.
     */
    val description: String?

    /**
     * The url of this embed.
     *
     * @return The url of this embed.
     */
    val url: URL?

    /**
     * The timestamp of this embed.
     *
     * @return The timestamp of this embed.
     */
    val timestamp: String?

    /**
     * The color of this embed.
     *
     * @return The color of this embed.
     */
    val color: Color?

    /**
     * The footer of this embed.
     *
     * @return The footer of this embed.
     */
    val footer: Footer?

    /**
     * The image of this embed.
     *
     * @return The image of this embed.
     */
    val image: Image?

    /**
     * The thumbnail of this embed.
     *
     * @return The thumbnail of this embed.
     */
    val thumbnail: Thumbnail?

    /**
     * The video of this embed.
     *
     * @return The video of this embed.
     */
    val video: Video?

    /**
     * The provider of this embed.
     *
     * @return The provider of this embed.
     */
    val provider: Provider?

    /**
     * The author of this embed.
     *
     * @return The author of this embed.
     */
    val author: Author?

    /**
     * The fields of this embed.
     *
     * @return The fields of this embed.
     */
    val fields: List<Field>
}
