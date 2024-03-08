plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    application
    `maven-publish`
}

apply(from = "gradle/tasks/GenerateRestMethods.gradle.kts")

dependencies {
    api(
        "com.fasterxml.jackson.module:jackson-module-kotlin:" +
            properties["jacksonModuleKotlinVersion"])

    // logger
    api("ch.qos.logback:logback-classic:" + properties["logBackClassicVersion"])
    api("ch.qos.logback:logback-core:" + properties["logBackCoreVersion"])
    api("uk.org.lidalia:sysout-over-slf4j:" + properties["sysoutOverSlf4jVersion"])

    // Http
    api("io.ktor:ktor-client-core-jvm:" + properties["ktorClientCoreJvmVersion"])

    // kotlin
    api(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:" +
            properties["kotlinxCoroutinesCoreVersion"])

    // annotations
    api("com.google.code.findbugs:jsr305:" + properties["jsr305Version"])
}

tasks {
    val compileKotlinTask = named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin")
    compileKotlinTask.configure { dependsOn("generateRestAPIMethodGetters") }
}
