package io.github.ydwk.yde.entities.util

import io.github.ydwk.yde.util.EntityToStringBuilder

/**
 * An entity that can be converted to a string using the [EntityToStringBuilder].
 */
interface ToStringEntity {

    /**
     * Converts this entity to a string using the [EntityToStringBuilder].
     *
     * @return The string representation of this entity.
     */
    override fun toString(): String
}