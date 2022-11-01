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

    override fun toString(): String {
        val name: String =
            if (this.name is String) {
                this.name as String
            } else {
                entity.javaClass.simpleName
            }

        val sb = StringBuilder(name)

        if (this.type != null) {
            sb.append('[').append(this.type).append(']')
        }

        if (this.name != null) {
            sb.append(':').append(this.name)
        }

        val snowflakeEntity = entity is SnowFlake
        if (snowflakeEntity) {
            val s: StringJoiner = StringJoiner(", ", "(", ")")
            s.add("id=${(entity as SnowFlake).id}")
            s.add("createTime=${(entity as SnowFlake).asTimestamp}")
            s.add("workerId=${(entity as SnowFlake).asWorkerId}")
            s.add("increment=${(entity as SnowFlake).asIncrement}")
            sb.append(s)
        }

        return sb.toString()
    }
}
