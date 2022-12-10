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
package io.github.ydwk.ydwk.slash

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.ydwk.ydwk.interaction.application.ApplicationCommandType
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class SlashTest {
    @Test
    fun toJson() {
        val json = ObjectMapper().createObjectNode()
        json.put("name", "test")
        json.put("description", "test")
        json.put("type", ApplicationCommandType.CHAT_INPUT.toInt())
        val options: ArrayNode = ObjectMapper().createArrayNode()
        options.add(
            ObjectMapper().createObjectNode().apply {
                put("name", "test")
                put("description", "test")
                put("type", SlashOptionType.STRING.toInt())
                put("required", true)
                set<ArrayNode>(
                    "choices",
                    ObjectMapper()
                        .createArrayNode()
                        .add(
                            ObjectMapper().createObjectNode().apply {
                                put("name", "test")
                                put("value", "test")
                            })
                        .add(
                            ObjectMapper().createObjectNode().apply {
                                put("name", "test")
                                put("value", 1)
                            })
                        .add(
                            ObjectMapper().createObjectNode().apply {
                                put("name", "test")
                                put("value", 1.0)
                            }))
            })
        json.set<ArrayNode>("options", options)

        // println(json.toPrettyString())
        assertEquals(json.get("name").asText(), "test")
        assertEquals(json.get("description").asText(), "test")
        assertEquals(json.get("type").asInt(), ApplicationCommandType.CHAT_INPUT.toInt())
        assertEquals(json.get("options").size(), 1)
        assertEquals(json.get("options").get(0).get("name").asText(), "test")
        assertEquals(json.get("options").get(0).get("description").asText(), "test")
        assertEquals(json.get("options").get(0).get("type").asInt(), SlashOptionType.STRING.toInt())
        assertEquals(json.get("options").get(0).get("required").asBoolean(), true)
        assertEquals(json.get("options").get(0).get("choices").size(), 3)
        assertEquals(json.get("options").get(0).get("choices").get(0).get("name").asText(), "test")
        assertEquals(json.get("options").get(0).get("choices").get(0).get("value").asText(), "test")
        assertEquals(json.get("options").get(0).get("choices").get(1).get("name").asText(), "test")
        assertEquals(json.get("options").get(0).get("choices").get(1).get("value").asInt(), 1)
        assertEquals(json.get("options").get(0).get("choices").get(2).get("name").asText(), "test")
        assertEquals(json.get("options").get(0).get("choices").get(2).get("value").asDouble(), 1.0)

        val actualJson =
            """
            {
              "name" : "test",
              "description" : "test",
              "type" : 1,
              "options" : [ {
                "name" : "test",
                "description" : "test",
                "type" : 3,
                "required" : true,
                "choices" : [ {
                  "name" : "test",
                  "value" : "test"
                }, {
                  "name" : "test",
                  "value" : 1
                }, {
                  "name" : "test",
                  "value" : 1.0
                } ]
              } ]
            }
        """.trimIndent()

        assertEquals(json.toPrettyString(), actualJson)
    }
}
