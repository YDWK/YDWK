/*
 * Copyright 2023 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package io.github.ydwk.yde.impl.rest.methods

import io.github.ydwk.yde.YDE
import io.github.ydwk.yde.entities.VoiceState
import io.github.ydwk.yde.impl.entities.VoiceStateImpl
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.methods.VoiceRestAPIMethods
import kotlinx.coroutines.CompletableDeferred

class VoiceRestAPIMethodsImpl(val yde: YDE) : VoiceRestAPIMethods {
    override fun requestVoiceRegions(): CompletableDeferred<List<VoiceState.VoiceRegion>> {
        return yde.restApiManager.get(EndPoint.VoiceEndpoint.GET_VOICE_REGIONS).execute {
            val voiceRegions: MutableList<VoiceState.VoiceRegion> = mutableListOf()
            val jsonBody = it.jsonBody
            if (jsonBody == null) {
                throw IllegalStateException("json body is null")
            } else {
                jsonBody.forEach { json ->
                    voiceRegions.add(VoiceStateImpl.VoiceRegionImpl(yde, json, json["id"].asLong()))
                }
            }
            voiceRegions
        }
    }
}
