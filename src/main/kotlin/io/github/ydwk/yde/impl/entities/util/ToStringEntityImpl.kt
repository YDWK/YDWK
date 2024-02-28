package io.github.ydwk.yde.impl.entities.util

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.util.ToStringEntity
import io.github.ydwk.yde.util.EntityToStringBuilder

open class ToStringEntityImpl<T : Any>(private val yde: YDE, private val clazz: Class<T>, private val fields : List<Any>? = null)  : ToStringEntity {
    override fun toString(): String {
        val entity = EntityToStringBuilder(yde, clazz)

        if (fields != null) {
            for (field in fields) {
                entity.add(field.toString(), field)
            }
        }

        entity.autoAddFields()

        return entity.toString()
    }
}