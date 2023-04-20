plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    application
    `maven-publish`
    signing
    jacoco // code coverage reports
    `kotlin-dsl`
}

repositories { mavenCentral() }

dependencies {
    api(project(":ydwk-core")) { isTransitive = true }

    // json
    api(
        "com.fasterxml.jackson.module:jackson-module-kotlin:" +
            properties["jacksonModuleKotlinVersion"])

    // logger
    api("ch.qos.logback:logback-classic:" + properties["logBackClassicVersion"])
    api("ch.qos.logback:logback-core:" + properties["logBackCoreVersion"])
    api("uk.org.lidalia:sysout-over-slf4j:" + properties["sysoutOverSlf4jVersion"])
    // YDE Entities
    api("io.github.realyusufismail:yde:" + properties["ydeVersion"])
}
