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
package io.github.ydwk.ydwk.voice.impl

import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.voice.VoiceLocation

class VoiceLocationImpl : VoiceLocation {
    override val ydwk: YDWK
        get() = TODO("Not yet implemented")

    override fun hasNext(): Boolean {
        TODO("Not yet implemented")
    }

    override fun next(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun isFinished(): Boolean {
        TODO("Not yet implemented")
    }

    override fun mute(): VoiceLocation {
        TODO("Not yet implemented")
    }

    override fun unmute(): VoiceLocation {
        TODO("Not yet implemented")
    }

    override fun isMuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun copy(): VoiceLocation {
        TODO("Not yet implemented")
    }
}
