package io.github.realyusufismail.ws.io.github.realyusufismail.cache

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.realyusufismail.ws.io.github.realyusufismail.cache.user.DummyUserImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.util.Objects
import kotlin.io.path.Path
import kotlin.test.assertEquals

class CacheTester {

    @Test
    @DisplayName("Test Cache")
    fun testCache() {
        val userJson = ObjectMapper().readTree(
            Files.readString(Path("src/test/kotlin/io/github/realyusufismail/cache/user/user.json")))

        val user = DummyUserImpl(userJson)
        val cache = DummyCache()

        cache[user.getIdLong()] = user

        val cachedUser = cache[user.getIdLong()] as DummyUserImpl

        if (Objects.deepEquals(user, cachedUser)) {
            println("User is cached")
        } else {
            println("User is not cached")
        }

        if (!Objects.deepEquals(user.getIdLong(), cachedUser.getIdLong())) {
            println("User ID is not cached")
        } else {
            println("User ID is cached")
        }

        if (!Objects.deepEquals(user.name, cachedUser.name)) {
            println("User name is not cached")
        } else {
            println("User name is cached")
        }

        //Assertions.assertEquals(user.getIdLong(), cachedUser.getIdLong(), "User ID is not equal")
        //Assertions.assertEquals(user.name, cachedUser.name, "User name is not equal")

        val newUserJson = ObjectMapper().readTree(
            Files.readString(Path("src/test/kotlin/io/github/realyusufismail/cache/user/newUser.json")))

        val oldName = cachedUser.name
        val newName = newUserJson["name"].asText()
        if (!Objects.deepEquals(oldName, newName)) {
            user.name = newName
        }

        if (Objects.deepEquals(user.name, cachedUser.name)) {
            println("User name is cached")
        } else {
            println("User name is not cached")
        }

        //Assertions.assertEquals(user.name, cachedUser.name, "User name is not equal")
    }
}