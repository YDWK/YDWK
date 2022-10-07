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
package io.github.ydwk.ydwk.entities.message.embed.builder

import io.github.ydwk.ydwk.entities.message.Embed
import java.awt.Color
import java.net.URL
import java.time.temporal.TemporalAccessor

interface EmbedBuilder {
    /**
     * Used to set the title of the embed.
     *
     * @param title The title of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setTitle(title: String): EmbedBuilder

    /**
     * Used to set the description of the embed.
     *
     * @param description The description of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setDescription(description: CharSequence): EmbedBuilder

    /**
     * Used to set the timestamp of the embed.
     *
     * @param timestamp The timestamp of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setTimestamp(timestamp: TemporalAccessor): EmbedBuilder

    /**
     * Used to set the url of the embed.
     *
     * @param url The url of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setUrl(url: URL): EmbedBuilder

    /**
     * Used to set the color of the embed.
     *
     * @param color The color of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setColor(color: Color): EmbedBuilder

    /**
     * Used to set the thumbnail of the embed.
     *
     * @param url The url of the thumbnail.
     * @return The current [EmbedBuilder] instance.
     */
    fun setThumbnail(url: URL): EmbedBuilder

    /**
     * Used to set the thumbnail of the embed.
     *
     * @param thumbnail The thumbnail of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setThumbnail(thumbnail: EmbedThumbnailBuilder): EmbedBuilder

    /**
     * Used to set the image of the embed.
     *
     * @param url The url of the image.
     * @return The current [EmbedBuilder] instance.
     */
    fun setImage(url: URL): EmbedBuilder

    /**
     * Used to set the image of the embed.
     *
     * @param image The image of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setImage(image: EmbedImageBuilder): EmbedBuilder

    /**
     * Used to set the footer of the embed.
     *
     * @param text The text of the footer.
     * @param iconUrl The url of the icon.
     * @return The current [EmbedBuilder] instance.
     */
    fun setFooter(text: String, iconUrl: URL): EmbedBuilder

    /**
     * Used to set the footer of the embed.
     *
     * @param footer The footer of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setFooter(footer: EmbedFooterBuilder): EmbedBuilder

    /**
     * Used to set the author of the embed.
     *
     * @param name The name of the author.
     * @param url The url of the author.
     * @param iconUrl The url of the icon.
     * @return The current [EmbedBuilder] instance.
     */
    fun setAuthor(name: String, url: URL? = null, iconUrl: URL? = null): EmbedBuilder

    /**
     * Used to set the author of the embed.
     *
     * @param author The author of the embed.
     * @return The current [EmbedBuilder] instance.
     */
    fun setAuthor(author: EmbedAuthorBuilder): EmbedBuilder

    /**
     * Used to add a field to the embed.
     *
     * @param name The name of the field.
     * @param value The value of the field.
     * @param inline Whether the field should be inline.
     * @return The current [EmbedBuilder] instance.
     */
    fun addField(name: String, value: CharSequence, inline: Boolean = true): EmbedBuilder

    /**
     * Used to add a field to the embed.
     *
     * @param field The field to add.
     * @return The current [EmbedBuilder] instance.
     */
    fun addField(field: EmbedFieldBuilder): EmbedBuilder

    /**
     * Used to build the embed.
     *
     * @return The [Embed] instance.
     */
    fun build(): Embed
}
