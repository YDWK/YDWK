package io.github.ydwk.yde.impl.entities.interaction.selectmenu

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.Emoji
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenu
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuOption
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl

class SelectMenuOptionImpl(
    override val yde: YDE,
    override val json: JsonNode,
    override val label: String,
    override val value: String,
    override val description: String?,
    override val emoji: Emoji?,
    override val default: Boolean,
) : SelectMenuOption, ToStringEntityImpl<SelectMenuOption>(yde, SelectMenuOption::class.java)