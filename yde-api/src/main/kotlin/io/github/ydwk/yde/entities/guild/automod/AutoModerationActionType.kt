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
package io.github.ydwk.yde.entities.guild.automod

enum class AutoModerationActionType(private val value: Int) {
  BLOCK_MESSAGE(1),
  SEND_ALERT_MESSAGE(2),
  TIMEOUT(3),
  BLOCK_MEMBER_INTERACTION(4),
  UNKNOWN(-1);

  fun getValue(): Int = value

  companion object {
    fun fromValue(value: Int): AutoModerationActionType =
      entries.firstOrNull { it.value == value } ?: UNKNOWN
  }
}
