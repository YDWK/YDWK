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
package io.github.ydwk.ydwk.util

import java.util.*

class EntityToStringBuilder(val entity: Any) {
  private var type: Any? = null
  private var name: String? = null
  private var customFields: MutableMap<String, Any?> = HashMap()

  fun type(type: Any): EntityToStringBuilder {
    this.type = type
    return this
  }

  fun type(type: Enum<*>): EntityToStringBuilder {
    this.type = type
    return this
  }

  fun name(name: String): EntityToStringBuilder {
    this.name = name
    return this
  }

  fun add(field: String, value: Any?): EntityToStringBuilder {
    customFields[field] = value
    return this
  }

  override fun toString(): String {
    val name: String =
      when (this.entity) {
        is String -> {
          entity
        }
        is Class<*> -> {
          cleanUpClassName(entity)
        }
        else -> {
          cleanUpClassName(entity.javaClass)
        }
      }

    val sb = StringBuilder(name)

    if (this.type != null) {
      sb.append('[').append(this.type).append(']')
    }

    if (this.name != null) {
      sb.append(':').append(this.name)
    }

    if (customFields.isNotEmpty()) {
      sb.append('{')
      for ((key, value) in customFields) {
        sb.append(key).append('=').append(value).append(',')
      }
      sb.setCharAt(sb.length - 1, '}')
    }

    val snowflakeEntity = entity is SnowFlake
    if (snowflakeEntity) {
      sb.append('(')
      sb.append("id=").append((entity as SnowFlake).id).append(',')
      sb.append("createTime=").append(entity.asTimestamp).append(',')
      sb.append("workerId=").append(entity.asWorkerId).append(',')
      sb.append("increment=").append(entity.asIncrement)
      sb.append(')')
    }

    return sb.toString()
  }

  private fun cleanUpClassName(clazz: Class<*>): String {
    return clazz.simpleName.replace("Impl", "")
  }
}
