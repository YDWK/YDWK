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
    api(project(":ydwk-event-manager"))

    // json
    api(
        "com.fasterxml.jackson.module:jackson-module-kotlin:" +
            properties["jacksonModuleKotlinVersion"])

    // config.json
    api("io.github.realyusufismail:jconfig:" + properties["jconfigVersion"])

    // logger
    api("ch.qos.logback:logback-classic:" + properties["logBackClassicVersion"])
    api("ch.qos.logback:logback-core:" + properties["logBackCoreVersion"])
    api("uk.org.lidalia:sysout-over-slf4j:" + properties["sysoutOverSlf4jVersion"])

    // ws and https
    api("com.squareup.okhttp3:okhttp:" + properties["okhttp3Version"])
    api("com.neovisionaries:nv-websocket-client:" + properties["nvWebsocketClientVersion"])
    api("com.codahale:xsalsa20poly1305:" + properties["xsalsa20poly1305Version"])

    // YDE Entities
    api("io.github.realyusufismail:yde:" + properties["ydeVersion"])

    // kotlin
    api(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:" +
            properties["kotlinxCoroutinesCoreVersion"])

    // annotations
    implementation("com.google.code.findbugs:jsr305:" + properties["jsr305Version"])

    // test
    testImplementation("org.jetbrains.kotlin:kotlin-test:" + properties["kotlinTestVersion"])
}
