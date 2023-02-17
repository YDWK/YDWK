package io.github.ydwk.ydwk.evm.listeners

import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent

interface ChannelListeners {
    /**
     * Listens to ChannelCreateEvent
     *
     * @param event The ChannelCreateEvent
     */
    fun onChannelCreate(event: ChannelCreateEvent) {}
}