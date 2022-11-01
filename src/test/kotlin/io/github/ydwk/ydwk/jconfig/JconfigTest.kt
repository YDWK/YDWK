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
package io.github.ydwk.ydwk.jconfig

import io.github.realyusufismail.jconfig.classes.JConfigBuilder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test

class JconfigTest {

    @Test
    fun test() {
        val jconfig =
            JConfigBuilder()
                .setDirectoryPath("src/test/resources/jconfig")
                .setFilename("sample")
                .build()

        val sampleToken: Int? = jconfig["sample_token"]?.asInt

        assertNotNull(sampleToken)
        assertEquals(21, sampleToken)
    }
}
