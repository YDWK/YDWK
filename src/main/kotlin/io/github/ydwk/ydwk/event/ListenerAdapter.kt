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
package io.github.ydwk.ydwk.event

import io.github.ydwk.ydwk.event.backend.ClassWalker
import io.github.ydwk.ydwk.event.backend.event.GenericEvent
import io.github.ydwk.ydwk.event.backend.event.IEventListener
import io.github.ydwk.ydwk.event.events.DisconnectEvent
import io.github.ydwk.ydwk.event.events.ReadyEvent
import io.github.ydwk.ydwk.event.events.ResumeEvent
import io.github.ydwk.ydwk.event.events.ShutDownEvent
import io.github.ydwk.ydwk.event.events.channel.ChannelCreateEvent
import io.github.ydwk.ydwk.event.events.channel.ChannelDeleteEvent
import io.github.ydwk.ydwk.event.events.interaction.*
import io.github.ydwk.ydwk.event.update.IEventUpdate
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class ListenerAdapter : IEventListener {

    /** Listens to all events */
    open fun onGenericEvent(event: GenericEvent) {}

    /** Listens to all event updates */
    open fun onGenericUpdate(eventUpdate: IEventUpdate<*, *>) {}

    /**
     * Listens to ready event
     *
     * @param event The ready event
     */
    open fun onReady(event: ReadyEvent) {}

    /**
     * Listens to Disconnect event
     *
     * @param event The disconnect event
     */
    open fun onDisconnect(event: DisconnectEvent) {}

    /**
     * Listens to Resume event
     *
     * @param event The resume event
     */
    open fun onResume(event: ResumeEvent) {}

    /**
     * Listens to ShutDown event
     *
     * @param event The shutdown event
     */
    open fun onShutDown(event: ShutDownEvent) {}

    // interactions

    /**
     * Listens to SlashCommandEvent
     *
     * @param event The SlashCommandEvent
     */
    open fun onSlashCommand(event: SlashCommandEvent) {}

    /**
     * Listens to AutoCompleteSlashCommandEvent
     *
     * @param event The AutoCompleteSlashCommandEvent
     */
    open fun onAutoCompleteSlashCommand(event: AutoCompleteSlashCommandEvent) {}

    /**
     * Listens to Model Event
     *
     * @param event The Model Event
     */
    open fun onModel(event: ModelEvent) {}

    /**
     * Listens to Ping Event
     *
     * @param event The Ping Event
     */
    open fun onPing(event: PingEvent) {}

    /**
     * Listens to Message Component Event
     *
     * @param event The Message Component Event
     */
    open fun onMessageComponent(event: MessageComponentEvent) {}

    // Channel
    /**
     * Listens to ChannelCreateEvent
     *
     * @param event The ChannelCreateEvent
     */
    open fun onChannelCreate(event: ChannelCreateEvent) {}

    /**
     * Listens to ChannelDeleteEvent
     *
     * @param event The ChannelDeleteEvent
     */
    open fun onChannelDelete(event: ChannelDeleteEvent) {}

    /**
     * This method is called when an event is received.
     *
     * @param event The event that was received.
     */
    override fun onEvent(event: GenericEvent) {
        onGenericEvent(event)

        if (event is IEventUpdate<*, *>) onGenericUpdate((event as IEventUpdate<*, *>))

        for (clazz in ClassWalker.range(event.javaClass, Event::class.java)) {
            if (unresolved!!.contains(clazz)) continue
            val mh = methods.computeIfAbsent(clazz, Companion::findMethod)
            if (mh == null) {
                unresolved!!.add(clazz)
                continue
            }
            try {
                mh.invoke(this, event)
            } catch (throwable: Throwable) {
                if (throwable is RuntimeException) throw throwable
                if (throwable is Error) throw throwable
                throw IllegalStateException(throwable)
            }
        }
    }

    companion object {
        private val lookup = MethodHandles.lookup()
        private val methods: ConcurrentMap<Class<*>, MethodHandle> = ConcurrentHashMap()
        private var unresolved: MutableSet<Class<*>>? = null

        init {
            unresolved = ConcurrentHashMap.newKeySet()
            unresolved?.let {
                Collections.addAll(
                    it,
                    Any::class.java, // Objects aren't events
                    Event::class.java, // onEvent is final and would never be found
                    IEventUpdate::class.java, // onBasicUpdate has already been called
                    GenericEvent::class.java // onBasicEvent has already been called
                    )
            }
        }

        private fun findMethod(clazz: Class<*>): MethodHandle? {
            var name = clazz.simpleName
            val type = MethodType.methodType(Void.TYPE, clazz)
            try {
                name = "on" + name.substring(0, name.length - "Event".length)
                return lookup.findVirtual(ListenerAdapter::class.java, name, type)
            } catch (
                ignored: NoSuchMethodException) {} // this means this is probably a custom event!
            catch (ignored: IllegalAccessException) {}
            return null
        }
    }
}
