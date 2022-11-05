package io.github.ydwk.ydwk.evm.backend.update

import io.github.ydwk.ydwk.YDWK

class EventUpdateGenerator<E, T>(private val ydwk: YDWK, private val entityType : UpdateEventType, private val old: T, private val new: T) {
    fun generate() : EventUpdateGenerator<E, T> {
        return this
    }
}