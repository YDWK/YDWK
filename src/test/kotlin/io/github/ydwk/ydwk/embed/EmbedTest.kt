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
package io.github.ydwk.ydwk.embed

import io.github.ydwk.yde.entities.message.embed.builder.EmbedImageBuilder
import io.github.ydwk.yde.impl.entities.message.embed.builder.EmbedBuilderImpl
import io.github.ydwk.ydwk.YDWK
import io.github.ydwk.ydwk.impl.YDWKImpl
import java.net.URL
import java.time.Instant
import kotlin.test.assertEquals
import okhttp3.OkHttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

class EmbedTest {

    @Mock
    lateinit var ydwk: YDWKImpl

    @Mock
    lateinit var client: OkHttpClient

    @BeforeEach
    fun setUp() {
        client = mock(OkHttpClient::class.java)
        ydwk = YDWKImpl(client)
    }

    @Test
    fun testEmbed() {
        val embed =
            EmbedBuilderImpl(ydwk)
                .setTitle("test")
                .setDescription("test")
                .setUrl(URL("https://www.google.com"))
                .setTimestamp(Instant.now())
                .setImage(EmbedImageBuilder(URL("https://www.google.com")))
                .setAuthor("test", URL("https://www.google.com"), URL("https://www.google.com"))
                .setFooter("test", URL("https://www.google.com"))
                .addField("test", "test", true)
                .build()

        assertEquals("test", embed.title)
        assertEquals("test", embed.description)
    }
}
