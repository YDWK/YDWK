package io.github.ydwk.yde.impl.entities.interaction.selectmenu

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.channel.enums.ChannelType
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenu
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuDefaultValues
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption
import io.github.ydwk.yde.impl.entities.interaction.ComponentImpl

class SelectMenuImpl(override val yde: YDE, override val json: JsonNode,
                     override val customId: String,
                     override val placeholder: String?,
                     override val minValues: Int?,
                     override val maxValues: Int?,
                     override val isDisabled: Boolean?,
                     override val options: List<SelectMenuOption>?,
                     override val defaultValues: List<SelectMenuDefaultValues>?,
                     override val channelTypes: Set<ChannelType>?
) : SelectMenu, ComponentImpl(yde.entityInstanceBuilder.buildComponent(json))