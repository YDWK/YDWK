repositories { mavenCentral() }

dependencies {
    api(project(":ydwk-core")) { isTransitive = true }
    api(project(":ydwk-event-manager")) { isTransitive = true }
    api(project(":ydwk-websocket")) { isTransitive = true }

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
    api("com.google.code.findbugs:jsr305:" + properties["jsr305Version"])

    // test
    testApi("org.jetbrains.kotlin:kotlin-test:" + properties["kotlinTestVersion"])
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(11) }
