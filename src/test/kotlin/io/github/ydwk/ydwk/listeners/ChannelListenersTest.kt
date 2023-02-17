package io.github.ydwk.ydwk.listeners

import io.github.realyusufismail.jconfig.util.JConfigUtils
import io.github.ydwk.ydwk.Activity
import io.github.ydwk.ydwk.BotBuilder
import io.github.ydwk.ydwk.evm.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.evm.listeners.ChannelListeners

class ChannelListenersTest : ChannelListeners {
    override fun onChannelCreate(event: ChannelCreateEvent) {
       println("Channel")
    }
}

suspend fun main() {
    val ydwk =
        BotBuilder.createDefaultBot(JConfigUtils.getString("token") ?: throw Exception("Token not found!"))
            .setActivity(Activity.playing("YDWK"))
            .setETFInsteadOfJson(true)
            .build()

    ydwk.awaitReady().addEvent(ChannelListenersTest())
}