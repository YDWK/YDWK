package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.events.voice.VoiceConnectionEvent

interface VoiceListeners : IEventListener {
    /**
     * Listens to VoiceConnectionEvent
     *
     * @param event The VoiceConnectionEvent
     */
    fun onVoiceConnection(event: VoiceConnectionEvent) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is VoiceConnectionEvent -> onVoiceConnection(event)
        }
    }
}