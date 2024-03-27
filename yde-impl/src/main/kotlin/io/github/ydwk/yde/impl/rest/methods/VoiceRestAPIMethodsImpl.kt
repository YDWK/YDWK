/*
 * Copyright 2024 YDWK inc.
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
import io.github.ydwk.yde.rest.EndPoint
import io.github.ydwk.yde.rest.RestResult
import io.github.ydwk.yde.rest.json
import io.github.ydwk.yde.rest.methods.VoiceRestAPIMethods

class VoiceRestAPIMethodsImpl(val yde: YDE) : VoiceRestAPIMethods {
    override suspend fun requestVoiceRegions(): RestResult<List<VoiceState.VoiceRegion>> {
        return yde.restApiManager.get(EndPoint.VoiceEndpoint.GET_VOICE_REGIONS).execute {
            val voiceRegions: MutableList<VoiceState.VoiceRegion> = mutableListOf()
            val jsonBody = it.json(yde)
            jsonBody.forEach { json ->
                voiceRegions.add(yde.entityInstanceBuilder.buildVoiceRegion(json))
            }
            voiceRegions
        }
    }
}
