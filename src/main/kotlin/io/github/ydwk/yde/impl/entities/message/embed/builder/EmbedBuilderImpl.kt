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
package io.github.ydwk.yde.impl.entities.message.embed.builder

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.message.Embed
import io.github.ydwk.yde.entities.message.embed.builder.*
import io.github.ydwk.yde.impl.entities.message.EmbedImpl
import io.github.ydwk.yde.util.Checks
import io.github.ydwk.yde.util.toOffsetDateTime
import java.awt.Color
import java.net.URL
import java.time.OffsetDateTime
import java.time.temporal.TemporalAccessor

class EmbedBuilderImpl(private val yde: YDE) : EmbedBuilder {
    private var title: String? = null
    private var description: StringBuilder = StringBuilder()
    private var timestamp: OffsetDateTime? = null
    private var url: String? = null
    private var color: Color? = null
    private var thumbnail: EmbedThumbnailBuilder? = null
    private var image: EmbedImageBuilder? = null
    private var author: EmbedAuthorBuilder? = null
    private var footer: EmbedFooterBuilder? = null
    private var fields: MutableList<EmbedFieldBuilder> = mutableListOf()

    override fun setTitle(title: String): EmbedBuilder {
        Checks.checkLength(title, 256, "Embed title needs to be 256 characters or less")
        this.title = title
        return this
    }

    override fun setDescription(description: CharSequence): EmbedBuilder {
        Checks.checkLength(
            description, 4096, "Embed description needs to be 4096 characters or less")
        this.description = StringBuilder(description)
        return this
    }

    override fun setTimestamp(timestamp: TemporalAccessor): EmbedBuilder {
        this.timestamp = toOffsetDateTime(timestamp)
        return this
    }

    override fun setUrl(url: URL): EmbedBuilder {
        this.url = url.toString()
        return this
    }

    override fun setColor(color: Color): EmbedBuilder {
        this.color = color
        return this
    }

    override fun setThumbnail(url: URL): EmbedBuilder {
        this.thumbnail = EmbedThumbnailBuilder(url)
        return this
    }

    override fun setThumbnail(thumbnail: EmbedThumbnailBuilder): EmbedBuilder {
        this.thumbnail = thumbnail
        return this
    }

    override fun setImage(url: URL): EmbedBuilder {
        this.image = EmbedImageBuilder(url)
        return this
    }

    override fun setImage(image: EmbedImageBuilder): EmbedBuilder {
        this.image = image
        return this
    }

    override fun setFooter(text: String, iconUrl: URL): EmbedBuilder {
        this.footer = EmbedFooterBuilder(text, iconUrl)
        return this
    }

    override fun setFooter(footer: EmbedFooterBuilder): EmbedBuilder {
        this.footer = footer
        return this
    }

    override fun setAuthor(name: String, url: URL?, iconUrl: URL?): EmbedBuilder {
        this.author = EmbedAuthorBuilder(name, url, iconUrl)
        return this
    }

    override fun setAuthor(author: EmbedAuthorBuilder): EmbedBuilder {
        this.author = author
        return this
    }

    override fun addField(name: String, value: CharSequence, inline: Boolean): EmbedBuilder {
        this.fields.add(EmbedFieldBuilder(name, value, inline))
        return this
    }

    override fun addField(field: EmbedFieldBuilder): EmbedBuilder {
        this.fields.add(field)
        return this
    }

    override fun build(): Embed {
        val json = yde.objectNode
        val thumbnailJson = yde.objectNode
        val imageJson = yde.objectNode
        val authorJson = yde.objectNode
        val footerJson = yde.objectNode
        val fieldsJson = yde.objectNode.arrayNode()

        if (title != null) json.put("title", title)
        if (description.isNotEmpty()) json.put("description", description.toString())
        if (timestamp != null) json.put("timestamp", timestamp.toString())
        if (url != null) json.put("url", url)
        if (color != null) json.put("color", color!!.rgb and 0xFFFFFF)

        when {
            thumbnail != null -> {
                thumbnailJson.put("url", thumbnail!!.url.toString())
                if (thumbnail!!.proxyUrl != null)
                    thumbnailJson.put("proxy_url", thumbnail!!.proxyUrl.toString())
                if (thumbnail!!.height != null) thumbnailJson.put("height", thumbnail!!.height)
                if (thumbnail!!.width != null) thumbnailJson.put("width", thumbnail!!.width)
            }
            image != null -> {
                imageJson.put("url", image!!.url.toString())
                if (image!!.proxyUrl != null)
                    imageJson.put("proxy_url", image!!.proxyUrl.toString())
                if (image!!.height != null) imageJson.put("height", image!!.height)
                if (image!!.width != null) imageJson.put("width", image!!.width)
            }
            author != null -> {
                authorJson.put("name", author!!.name)
                if (author!!.url != null) authorJson.put("url", author!!.url.toString())
                if (author!!.iconUrl != null)
                    authorJson.put("icon_url", author!!.iconUrl.toString())
                if (author!!.proxyIconUrl != null)
                    authorJson.put("proxy_icon_url", author!!.proxyIconUrl.toString())
            }
            footer != null -> {
                footerJson.put("text", footer!!.text)
                if (footer!!.iconUrl != null)
                    footerJson.put("icon_url", footer!!.iconUrl.toString())
                if (footer!!.proxyIconUrl != null)
                    footerJson.put("proxy_icon_url", footer!!.proxyIconUrl.toString())
            }
            fields.isNotEmpty() -> {
                for (field in fields) {
                    val fieldJson = yde.objectNode
                    fieldJson.put("name", field.name)
                    fieldJson.put("value", field.value.toString())
                    fieldJson.put("inline", field.inline)
                    fieldsJson.add(fieldJson)
                }
            }
        }

        if (thumbnailJson.size() > 0) json.set<JsonNode>("thumbnail", thumbnailJson)
        if (imageJson.size() > 0) json.set<JsonNode>("image", imageJson)
        if (footerJson.size() > 0) json.set<JsonNode>("footer", footerJson)
        if (authorJson.size() > 0) json.set<JsonNode>("author", authorJson)
        if (fieldsJson.size() > 0) json.set<JsonNode>("fields", fieldsJson)

        return EmbedImpl(yde, json)
    }
}
