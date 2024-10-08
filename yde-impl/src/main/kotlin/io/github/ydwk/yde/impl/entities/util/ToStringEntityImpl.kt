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
package io.github.ydwk.yde.impl.entities.util

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.util.ToStringEntity
import io.github.ydwk.yde.util.EntityToStringBuilder

open class ToStringEntityImpl<T : Any>(
    private val yde: YDE,
    private val clazz: Class<T>,
    private val fields: List<Any>? = null
) : ToStringEntity {

    override fun toString(): String {
        val entity = EntityToStringBuilder(yde, clazz)

        if (fields != null) {
            for (field in fields) {
                entity.add(field.toString(), field)
            }
        }

        return entity.toString()
    }

    fun buildString(builderAction: EntityToStringBuilder.() -> Unit): String {
        val entity = EntityToStringBuilder(yde, clazz)
        entity.builderAction()
        return entity.toString()
    }
}
