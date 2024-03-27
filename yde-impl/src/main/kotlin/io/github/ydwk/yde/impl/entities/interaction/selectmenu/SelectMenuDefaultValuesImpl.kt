package io.github.ydwk.yde.impl.entities.interaction.selectmenu

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.interaction.selectmenu.SelectMenuDefaultValues
import io.github.ydwk.yde.impl.entities.util.ToStringEntityImpl

class SelectMenuDefaultValuesImpl(override val yde: YDE, override val json: JsonNode,
                                  override val type: SelectMenuDefaultValues.Type
) : SelectMenuDefaultValues, ToStringEntityImpl<SelectMenuDefaultValues>(yde, SelectMenuDefaultValues::class.java)