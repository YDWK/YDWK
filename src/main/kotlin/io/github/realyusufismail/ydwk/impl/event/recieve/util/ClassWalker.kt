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
package io.github.realyusufismail.ydwk.impl.event.recieve.util

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import java.util.*
import kotlin.reflect.KClass

class ClassWalker
private constructor(private val clazz: KClass<*>, private val end: KClass<*> = Any::class) :
    Iterable<KClass<*>> {
    override fun iterator(): MutableIterator<KClass<*>> {
        return object : MutableIterator<KClass<*>> {
            private val done: MutableSet<KClass<*>> = HashSet()
            private val work: Deque<KClass<*>> = LinkedList()

            init {
                work.addLast(clazz)
                done.add(end)
            }

            override fun hasNext(): Boolean {
                return !work.isEmpty()
            }

            override fun next(): KClass<*> {
                val current = work.removeFirst()
                done.add(current)
                for (parent in current.nestedClasses) {
                    if (!done.contains(parent)) work.addLast(parent)
                }
                // cast to Any to avoid kotlin compiler bug

                if (current.java.isKotlinClass()) {
                    val parent =
                        current.supertypes.firstOrNull { it.classifier is KClass<*> }?.classifier
                            as KClass<*>?
                    if (parent != null && !done.contains(parent)) work.addLast(parent)
                } else {
                    throw UnsupportedOperationException("Java classes are not supported")
                }
                return current
            }

            override fun remove() {
                work.removeFirst()
                done.remove(end)
            }
        }
    }

    companion object {
        fun range(start: KClass<*>, end: KClass<*>): ClassWalker {
            return ClassWalker(start, end)
        }

        fun walk(start: KClass<*>): ClassWalker {
            return ClassWalker(start)
        }
    }
}
