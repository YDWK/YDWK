package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.backend.event.GenericEvent
import io.github.ydwk.ydwk.evm.backend.event.IEventListener
import io.github.ydwk.ydwk.evm.event.events.ban.GuildBanAddEvent

interface  GuildModerationListeners : IEventListener {
    /**
     * Listens to GuildBanAddEvent
     *
     * @param event The GuildBanAddEvent
     */
    fun onGuildBanAdd(event: GuildBanAddEvent) {}

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is GuildBanAddEvent -> onGuildBanAdd(event)
        }
    }
}