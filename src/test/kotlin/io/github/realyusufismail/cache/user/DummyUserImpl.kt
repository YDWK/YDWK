package io.github.realyusufismail.ws.io.github.realyusufismail.cache.user

import com.fasterxml.jackson.databind.JsonNode

class DummyUserImpl(val json : JsonNode) : DummyUser {

    override fun getIdLong(): Long {
        return json.get("id").asLong()
    }

    override var name: String = json.get("name").asText()
}