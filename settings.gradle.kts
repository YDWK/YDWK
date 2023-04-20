rootProject.name = "ydwk"

pluginManagement {
    val jvmVersion: String by settings
    val pluginAllOpenVersion: String by settings
    val spotlessVersion: String by settings
    val dokkaVersion: String by settings
    val detektVersion: String by settings

    plugins {
        kotlin("jvm") version jvmVersion
        kotlin("plugin.allopen") version pluginAllOpenVersion
        id("com.diffplug.spotless") version spotlessVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
    }
}

gradle.settingsEvaluated {
    include("ydwk-core", "ydwk-event-manager")
}
