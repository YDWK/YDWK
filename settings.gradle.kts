rootProject.name = "ydwk"

pluginManagement {
    val jvmVersion: String by settings
    val pluginAllOpenVersion: String by settings
    val spotlessVersion: String by settings
    val dokkaVersion: String by settings
    val benManesVersion: String by settings

    plugins {
        kotlin("jvm") version jvmVersion
        kotlin("plugin.allopen") version pluginAllOpenVersion
        id("com.diffplug.spotless") version spotlessVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("com.github.ben-manes.versions") version benManesVersion
        application
        `maven-publish`
        signing
        jacoco // code coverage reports
    }
}

include("yde-api", "yde-impl", "ydwk")
