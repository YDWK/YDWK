rootProject.name = "ydwk"

pluginManagement {
    val spotlessVersion: String by settings
    val dokkaVersion: String by settings
    val benManesVersion: String by settings

    plugins {
        id("com.diffplug.spotless") version spotlessVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("com.github.ben-manes.versions") version benManesVersion
    }
}

include("yde-api", "yde-impl", "ydwk")
