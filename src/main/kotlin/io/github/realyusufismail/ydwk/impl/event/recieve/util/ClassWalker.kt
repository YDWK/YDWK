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

import java.util.*

class ClassWalker
private constructor(private val clazz: Class<*>, private val end: Class<*> = Any::class.java) :
    Iterable<Class<*>?> {
    override fun iterator(): MutableIterator<Class<*>?> {
        return object : MutableIterator<Class<*>?> {
            private val done: MutableSet<Class<*>> = HashSet()
            private val work: Deque<Class<*>> = LinkedList()

            init {
                work.addLast(clazz)
                done.add(end)
            }

            override fun hasNext(): Boolean {
                return !work.isEmpty()
            }

            override fun next(): Class<*> {
                val current = work.removeFirst()
                done.add(current)
                for (parent in current.interfaces) {
                    if (!done.contains(parent)) work.addLast(parent)
                }
                val parent = current.superclass
                if (parent != null && !done.contains(parent)) work.addLast(parent)
                return current
            }

            override fun remove() {
                work.removeFirst()
                done.remove(end)
            }
        }
    }

    companion object {
        fun range(start: Class<*>, end: Class<*>): ClassWalker {
            return ClassWalker(start, end)
        }

        fun walk(start: Class<*>): ClassWalker {
            return ClassWalker(start)
        }
    }
}
