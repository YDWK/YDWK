package io.github.ydwk.ydwk.bot

import io.github.realyusufismail.jconfig.JConfig

class CustomConfig {
    private val config = JConfig.builder().setDirectoryPath("");

    fun build() : JConfig {
        return config.build()
    }
}

fun main() {
    val config = CustomConfig().build()

    println(config["name"].asString)
}