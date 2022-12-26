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
package io.github.ydwk.ydwk.impl.interaction.message.selectmenu

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.entities.channel.enums.ChannelType
import io.github.ydwk.ydwk.entities.message.Embed
import io.github.ydwk.ydwk.impl.entities.MessageImpl
import io.github.ydwk.ydwk.impl.interaction.ComponentInteractionImpl
import io.github.ydwk.ydwk.impl.interaction.application.sub.ReplyImpl
import io.github.ydwk.ydwk.impl.interaction.message.ComponentImpl
import io.github.ydwk.ydwk.impl.interaction.message.selectmenu.types.StringSelectMenuImpl
import io.github.ydwk.ydwk.interaction.application.sub.Reply
import io.github.ydwk.ydwk.interaction.message.ComponentType
import io.github.ydwk.ydwk.interaction.message.selectmenu.SelectMenu
import io.github.ydwk.ydwk.interaction.message.selectmenu.creator.SelectMenuCreator
import io.github.ydwk.ydwk.interaction.message.selectmenu.creator.types.*
import io.github.ydwk.ydwk.util.GetterSnowFlake

open class SelectMenuImpl(
    ydwk: YDWK,
    json: JsonNode,
    interactionId: GetterSnowFlake,
) : SelectMenu, ComponentInteractionImpl(ydwk, json, interactionId) {
    protected val componentJson: JsonNode =
        MessageImpl(ydwk, json["message"], json["message"]["id"].asLong()).json

    constructor(
        componentInteractionImpl: ComponentInteractionImpl
    ) : this(
        componentInteractionImpl.ydwk,
        componentInteractionImpl.json,
        componentInteractionImpl.interactionId)

    override val customId: String
        get() = componentJson["custom_id"].asText()

    override val placeholder: String?
        get() =
            if (componentJson.has("placeholder")) componentJson["placeholder"].asText() else null

    override val minValues: Int
        get() = componentJson["min_values"].asInt()

    override val maxValues: Int
        get() = componentJson["max_values"].asInt()
    override val values: List<String>
        get() = json["data"]["values"].map { it.asText() }

    override val isDisabled: Boolean
        get() = componentJson["disabled"].asBoolean()

    override fun reply(content: String): Reply {
        return ReplyImpl(ydwk, content, null, interactionId.asString, interactionToken)
    }

    override fun reply(embed: Embed): Reply {
        return ReplyImpl(ydwk, null, embed, interactionId.asString, interactionToken)
    }

    open class SelectMenuCreatorImpl(
        open val customId: String,
        val componentType: ComponentType,
        override val json: ObjectNode = JsonNodeFactory.instance.objectNode()
    ) : ISelectMenuCreator, SelectMenuCreator {
        private var placeholder: String? = null
        private var minValues: Int? = null
        private var maxValues: Int? = null
        private var disabled: Boolean = false

        override fun setPlaceholder(placeholder: String): SelectMenuCreator {
            this.placeholder = placeholder
            return this
        }

        override fun setMinValues(minValues: Int): SelectMenuCreator {
            this.minValues = minValues
            return this
        }

        override fun setMaxValues(maxValues: Int): SelectMenuCreator {
            this.maxValues = maxValues
            return this
        }

        override fun setDisabled(disabled: Boolean): SelectMenuCreator {
            this.disabled = disabled
            return this
        }

        override fun create(): ISelectMenuCreator {
            json.put("type", componentType.getType())
            json.put("custom_id", customId)
            if (placeholder != null) json.put("placeholder", placeholder)
            if (minValues != null) json.put("min_values", minValues)
            if (maxValues != null) json.put("max_values", maxValues)
            json.put("disabled", disabled)
            return this
        }
    }

    data class StringSelectMenuCreatorImpl(
        override val customId: String,
        val options: List<StringSelectMenuImpl.StringSelectMenuOptionCreator>
    ) : StringSelectMenuCreator, SelectMenuCreatorImpl(customId, ComponentType.STRING_SELECT_MENU) {
        init {
            json.putArray("options").addAll(options.map { it.json })
        }
    }

    data class RoleSelectMenuCreatorImpl(override val customId: String) :
        RoleSelectMenuCreator, SelectMenuCreatorImpl(customId, ComponentType.ROLE_SELECT_MENU)

    data class UserSelectMenuCreatorImpl(
        override val customId: String,
    ) : UserSelectMenuCreator, SelectMenuCreatorImpl(customId, ComponentType.USER_SELECT_MENU)

    data class ChannelSelectMenuCreatorImpl(
        override val customId: String,
        val channelTypes: List<ChannelType>
    ) :
        ChannelSelectMenuCreator,
        SelectMenuCreatorImpl(customId, ComponentType.CHANNEL_SELECT_MENU) {
        init {
            for (channelType in channelTypes) {
                json.putArray("channel_types").add(channelType.getId())
            }
        }
    }

    data class MemberSelectMenuCreatorImpl(
        override val customId: String,
    ) :
        MemberSelectMenuCreator,
        SelectMenuCreatorImpl(customId, ComponentType.MENTIONABLE_SELECT_MENU)

    sealed interface ISelectMenuCreator : ComponentImpl.ComponentCreator
}
