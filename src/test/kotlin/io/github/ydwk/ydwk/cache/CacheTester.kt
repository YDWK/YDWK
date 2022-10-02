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
package io.github.realyusufismail.ws.io.github.realyusufismail.cache

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.realyusufismail.ws.io.github.realyusufismail.cache.user.DummyUserImpl
import io.github.realyusufismail.ydwk.cache.CacheType
import java.nio.file.Files
import java.util.Objects
import kotlin.io.path.Path
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CacheTester {

    @Test
    @DisplayName("Test Cache")
    fun testCache() {
        val userJson =
            ObjectMapper()
                .readTree(
                    Files.readString(
                        Path("src/test/kotlin/io/github/realyusufismail/cache/user/user.json")))

        val user = DummyUserImpl(userJson)
        val cache = DummyCache()

        cache[user.id, user] = CacheType.USER

        // TODO: problem here
        val cachedUser = cache[user.id, CacheType.USER] as DummyUserImpl

        Assertions.assertEquals(user.idAsLong, cachedUser.idAsLong, "User ID is not equal")
        Assertions.assertEquals(user.name, cachedUser.name, "User name is not equal")

        val newUserJson =
            ObjectMapper()
                .readTree(
                    Files.readString(
                        Path("src/test/kotlin/io/github/realyusufismail/cache/user/newUser.json")))

        val oldName = cachedUser.name
        val newName = newUserJson["name"].asText()
        if (!Objects.deepEquals(oldName, newName)) {
            user.name = newName
        }

        Assertions.assertNotEquals(oldName, newName, "User name is equal")
        Assertions.assertEquals(user.name, cachedUser.name, "User name is not equal")
    }
}
