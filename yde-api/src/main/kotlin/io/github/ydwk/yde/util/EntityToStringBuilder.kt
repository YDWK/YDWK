/*
 * Copyright 2024 YDWK inc.
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
package io.github.ydwk.yde.util

import com.fasterxml.jackson.databind.JsonNode
import io.github.ydwk.yde.YDE
import java.util.*

class EntityToStringBuilder(val yde: YDE, val entity: Any) {
    private var type: Any? = null
    private var name: String? = null
    private var customFields: MutableMap<String, Any?> = HashMap()
    private var autoAddFields: Boolean = false

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

    fun autoAddFields(): EntityToStringBuilder {
        this.autoAddFields = true
        return this
    }

    override fun toString(): String {
        val mainJson = yde.objectMapper.createObjectNode()

        val name: String =
            when (this.entity) {
                is String -> {
                    cleanUpClassName(this.entity)
                }
                is Class<*> -> {
                    cleanUpClassName(entity)
                }
                else -> {
                    cleanUpClassName(entity.javaClass)
                }
            }

        mainJson.put("name", name)

        val subJson = yde.objectMapper.createObjectNode()

        if (this.type != null) {
            subJson.put("type", this.type.toString())
        }

        if (this.name != null) {
            subJson.put("name", this.name)
        }

        // add custom fields
        for ((key, value) in customFields) {
            subJson.put(key, value.toString())
        }

        // add snowflake
        val snowflakeEntity = entity is SnowFlake
        if (snowflakeEntity) {
            val snowflake = entity as SnowFlake
            subJson.put("id", snowflake.id)
            subJson.put("createTime", snowflake.asTimestamp)
            subJson.put("workerId", snowflake.asWorkerId)
            subJson.put("increment", snowflake.asIncrement)
        }

        if (autoAddFields) {
            val fieldJson = yde.objectMapper.createObjectNode()
            val fields = entity.javaClass.declaredFields
            for (field in fields) {
                field.isAccessible = true
                val value = field.get(entity)
                fieldJson.put(field.name, value.toString())
            }

            if (fieldJson.size() > 0) {
                subJson.set<JsonNode>("fields", fieldJson)
            }
        }

        if (subJson.size() > 0) {
            mainJson.set<JsonNode>("data", subJson)
        }

        return mainJson.toString()
    }

    private fun cleanUpClassName(clazz: Class<*>): String {
        return clazz.simpleName.replace("Impl", "")
    }

    private fun cleanUpClassName(name: String): String {
        return name.replace("Impl", "")
    }
}
