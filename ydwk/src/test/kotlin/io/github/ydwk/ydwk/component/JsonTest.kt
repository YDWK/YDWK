/*
 * Copyright 2024-2026 YDWK inc.
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
package io.github.ydwk.ydwk.component

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import io.github.ydwk.yde.interaction.sub.InteractionCallbackType
import io.github.ydwk.ydwk.testkit.assertJsonEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JsonTest {

  @Test
  @DisplayName("builds a component action row JSON payload")
  fun builds_component_action_row_json_payload() {
    val mainComponent = JsonNodeFactory.instance.arrayNode()

    mainComponent.add(
      JsonNodeFactory.instance
        .objectNode()
        .put("type", 1)
        .set(
          "components",
          JsonNodeFactory.instance.arrayNode().apply {
            add(JsonNodeFactory.instance.objectNode().put("type", 2).put("label", "test"))
          },
        ) as JsonNode
    )

    assertJsonEquals(
      """[{"type":1,"components":[{"type":2,"label":"test"}]}]""",
      mainComponent.toString(),
    )
  }

  @Test
  @DisplayName("builds a component array without explicit row type")
  fun builds_component_array_without_explicit_row_type() {
    val mainComponent = JsonNodeFactory.instance.arrayNode()

    mainComponent.add(
      JsonNodeFactory.instance
        .objectNode()
        .set(
          "components",
          JsonNodeFactory.instance.arrayNode().apply {
            add(JsonNodeFactory.instance.objectNode().put("type", 2).put("label", "test"))
          },
        ) as JsonNode
    )

    assertJsonEquals("""[{"components":[{"type":2,"label":"test"}]}]""", mainComponent.toString())
  }

  @Test
  @DisplayName("builds a full interaction callback body with components")
  fun builds_full_interaction_callback_body_with_components() {
    val mainBody =
      JsonNodeFactory.instance
        .objectNode()
        .put("type", InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE.getType())
    val secondBody = JsonNodeFactory.instance.objectNode()
    val mainComponent = JsonNodeFactory.instance.arrayNode()

    mainComponent.add(
      JsonNodeFactory.instance
        .objectNode()
        .set(
          "components",
          JsonNodeFactory.instance.arrayNode().apply {
            add(JsonNodeFactory.instance.objectNode().put("type", 2).put("label", "test"))
          },
        ) as JsonNode
    )

    secondBody.set<ArrayNode>("components", mainComponent)

    mainBody.set<JsonNode>("data", secondBody)

    assertJsonEquals(
      """{"type":4,"data":{"components":[{"components":[{"type":2,"label":"test"}]}]}}""",
      mainBody.toString(),
    )
  }
}
