/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
package io.github.realyusufismail.ydwk.event.recieve

import io.github.realyusufismail.ydwk.event.Event
import io.github.realyusufismail.ydwk.event.events.DisconnectEvent
import io.github.realyusufismail.ydwk.event.events.ReadyEvent
import io.github.realyusufismail.ydwk.event.events.ResumeEvent
import io.github.realyusufismail.ydwk.event.events.ShutDownEvent
import io.github.realyusufismail.ydwk.event.update.IEventUpdate
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.rmi.UnexpectedException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.reflect.KClass

abstract class ListenerAdapter : EventListener {

    /** Listens to all events */
    fun onBasicEvent(event: Event) {}

    /** Listens to all event updates */
    fun onBasicUpdate(eventUpdate: IEventUpdate<*, *>) {}

    /**
     * Listens to ready event
     *
     * @param event The ready event
     */
    fun onReady(event: ReadyEvent) {}

    /**
     * Listens to Disconnect event
     *
     * @param event The disconnect event
     */
    fun onDisconnect(event: DisconnectEvent) {}

    /**
     * Listens to Resume event
     *
     * @param event The resume event
     */
    fun onResume(event: ResumeEvent) {}

    /**
     * Listens to ShutDown event
     *
     * @param event The shutdown event
     */
    fun onShutDown(event: ShutDownEvent) {}

    /**
     * This method is called when an event is received.
     *
     * @param event The event that was received.
     */
    override suspend fun onEvent(event: Event) {
        onBasicEvent(event)
        if (event is IEventUpdate<*, *>) onBasicUpdate(event as IEventUpdate<*, *>)
        for (clazz in ClassWalker.range(event::class, Event::class)) {
            val ur = unresolved
            if (ur != null) {
                if (ur.contains(clazz)) continue
                val mh = methods.computeIfAbsent(clazz) { clazz: KClass<*> -> findMethod(clazz) }
                if (mh == null) {
                    ur.add(clazz)
                    continue
                }
                try {
                    mh.invoke(this, event)
                } catch (throwable: Throwable) {
                    if (throwable is RuntimeException) throw throwable
                    if (throwable is Error) throw throwable
                    throw IllegalStateException(throwable)
                }
            } else {
                throw UnexpectedException("unresolved is null")
            }
        }
    }

    companion object {
        private val lookup = MethodHandles.lookup()
        private val methods: ConcurrentMap<KClass<*>, MethodHandle?> = ConcurrentHashMap()
        private var unresolved: MutableSet<KClass<*>>? = null

        init {
            unresolved = ConcurrentHashMap.newKeySet()
            unresolved?.let {
                Collections.addAll(
                    it,
                    Any::class, // Objects aren't events
                    Event::class, // onEvent is final and would never be found
                    IEventUpdate::class, // onBasicUpdate has already been called
                    Event::class // onBasicEvent has already been called
                    )
            }
        }

        private fun findMethod(clazz: KClass<*>): MethodHandle? {
            var name = clazz.simpleName
            val type = MethodType.methodType(Void.TYPE, clazz.java)
            try {
                name = "on" + name!!.substring(0, name.length - "Event".length)
                return lookup.findVirtual(EventListener::class.java, name, type)
            } catch (
                ignored: NoSuchMethodException) {} // this means this is probably a custom event!
            catch (ignored: IllegalAccessException) {}
            return null
        }
    }
}