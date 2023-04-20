tasks.register("copyKotlinSources") {
    group = "generate"
    description = "Copies Kotlin source files from src/ to buildSrc/src/main/kotlin/"

    inputs.dir("src/main/kotlin")
    outputs.dir("buildSrc/src/main/kotlin")

    doLast {
        copy {
            from("ydwk-event-manager/src/main/kotlin") {
                include("**/*.kt")
            }
            into("buildSrc/src/main/kotlin")
        }
    }

    val gadlePropertiesFile = copy {
        from("gradle.properties")
        into("buildSrc")
    }

    val buildFile = File("buildSrc/build.gradle.kts")
    buildFile.writeText("""
        plugins {
            id("java")
        }
        
        repositories {
            mavenCentral()
        }
        
        dependencies {
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
    """.trimIndent())
}