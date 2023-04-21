repositories { mavenCentral() }

dependencies {
    compileOnly(project(":ydwk-event-manager")) { isTransitive = true }
    compileOnly(project(":ydwk-core")) { isTransitive = true }

    // json
    api(
        "com.fasterxml.jackson.module:jackson-module-kotlin:" +
            properties["jacksonModuleKotlinVersion"])

    // logger
    api("ch.qos.logback:logback-classic:" + properties["logBackClassicVersion"])
    api("ch.qos.logback:logback-core:" + properties["logBackCoreVersion"])
    api("uk.org.lidalia:sysout-over-slf4j:" + properties["sysoutOverSlf4jVersion"])

    // ws
    api("com.neovisionaries:nv-websocket-client:" + properties["nvWebsocketClientVersion"])
    api("com.codahale:xsalsa20poly1305:" + properties["xsalsa20poly1305Version"])
}

kotlin { jvmToolchain(11) }
