repositories { mavenCentral() }

dependencies {
    compileOnly(project(":ydwk-core")) { isTransitive = true }

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

    // kotlin
    api(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:" +
            properties["kotlinxCoroutinesCoreVersion"])
}

kotlin { jvmToolchain(11) }
