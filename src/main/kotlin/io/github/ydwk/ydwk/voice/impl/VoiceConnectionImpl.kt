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
import io.github.ydwk.ydwk.entities.VoiceState
import io.github.ydwk.ydwk.voice.VoiceConnection
import io.github.ydwk.ydwk.ws.voice.util.SpeakingFlag
import java.util.*
import java.util.concurrent.CompletableFuture

class VoiceConnectionImpl(
    val guildId: Long,
    val ydwk: YDWK,
    val future: CompletableFuture<VoiceConnectionImpl>
) : VoiceConnection {
    var token: String? = null
    var sessionId: String? = voiceState.sessionId
    var voiceEndpoint: String? = null
    var userId: Long? = null
    override val speakingFlags: EnumSet<SpeakingFlag> = EnumSet.noneOf(SpeakingFlag::class.java)

    override fun setDeafened(deafened: Boolean): VoiceConnection {
        TODO("Not yet implemented")
    }

    override fun setMuted(muted: Boolean): VoiceConnection {
        TODO("Not yet implemented")
    }

    override fun isPrioritySpeaker(): Boolean {
        return speakingFlags.contains(SpeakingFlag.PRIORITY)
    }

    override fun setPriority(priority: Boolean): VoiceConnection {
        val newSpeakingFlags = speakingFlags.clone()
        if (priority) {
            newSpeakingFlags.add(SpeakingFlag.PRIORITY)
        } else {
            newSpeakingFlags.remove(SpeakingFlag.PRIORITY)
        }
        speakingFlags.addAll(newSpeakingFlags)
        return this
    }

    override fun isSpeaking(): Boolean {
        return speakingFlags.contains(SpeakingFlag.MICROPHONE)
    }

    override fun setSpeaking(speaking: Boolean): VoiceConnection {
        val newSpeakingFlags = speakingFlags.clone()
        if (speaking) {
            newSpeakingFlags.add(SpeakingFlag.MICROPHONE)
        } else {
            newSpeakingFlags.remove(SpeakingFlag.MICROPHONE)
        }
        speakingFlags.addAll(newSpeakingFlags)
        return this
    }

    override fun disconnect(): VoiceConnection {
        TODO("Not yet implemented")
    }

    override val voiceState: VoiceState
        get() = TODO("Not yet implemented")
}
