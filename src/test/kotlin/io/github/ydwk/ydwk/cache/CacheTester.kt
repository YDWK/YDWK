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
package io.github.ydwk.ydwk.cache

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.ydwk.yde.cache.CacheIds
import io.github.ydwk.yde.cache.DummyCache
import io.github.ydwk.yde.impl.YDEImpl
import io.github.ydwk.ydwk.cache.user.DummyUserImpl
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class CacheTester {
    val yde = YDEImpl("", "", OkHttpClient(), mutableListOf(), "", "")
    private val objectMapper = ObjectMapper()
    private val cache = DummyCache(setOf(CacheIds.USER), yde)
    private val userJson =
        objectMapper.readTree(Files.readString(Path.of("src/test/resources/cache/user.json")))
    private val newUserJson =
        objectMapper.readTree(Files.readString(Path.of("src/test/resources/cache/newUser.json")))
    private val userId = "1000488829532440204"

    @Test
    @Order(1)
    fun storeCacheTest() {
        val user: DummyUser = DummyUserImpl(userJson)
        cache[userId, user] = CacheIds.USER
    }

    @Test
    @Order(2)
    fun checkCacheSizeTest() {
        storeCacheTest()
        assertNotNull(cache.size)
        assertEquals(1, cache.size)
    }

    @Test
    @Order(3)
    fun getCacheTest() {
        storeCacheTest()
        val user = cache[userId, CacheIds.USER]
        assertNotNull(user)
    }

    @Test
    @Order(3)
    fun updateCacheTest() {
        storeCacheTest()
        val user = DummyUserImpl(newUserJson)
        cache[user.id, user] = CacheIds.USER
        val updatedUser = cache[userId, CacheIds.USER] as DummyUserImpl
        assert(updatedUser.id == userId)
    }

    @Test
    @Order(4)
    fun deleteCacheTest() {
        storeCacheTest()
        cache.remove(userId, CacheIds.USER)
        assertEquals(0, cache.size)
        assertNull(cache[userId, CacheIds.USER])
    }

    @Test
    @Order(5)
    fun clearCacheTest() {
        storeCacheTest()
        cache.clear()
        assert(cache.size == 0)
    }
}
