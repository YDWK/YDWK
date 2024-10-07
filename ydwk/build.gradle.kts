plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    application
    `maven-publish`
}

apply(from = "gradle/tasks/generateEvents.gradle.kts")

apply(from = "gradle/tasks/checkEvents.gradle.kts")

apply(from = "gradle/tasks/eventClassJavaDocChecker.gradle")

dependencies {
    api(project(":yde-impl"))

    // config.json
    api("io.github.realyusufismail:jconfig:" + properties["jconfigVersion"])

    // ws and https
    api("com.neovisionaries:nv-websocket-client:" + properties["nvWebsocketClientVersion"])
    api("com.codahale:xsalsa20poly1305:" + properties["xsalsa20poly1305Version"])

    // decode Opus
    api("org.jitsi:libjitsi:" + properties["libjitsiVersion"])

    // files to bytes
    api("commons-io:commons-io:" + properties["commonsIoVersion"])

    // guava
    api("com.google.guava:guava:" + properties["guavaVersion"])
    implementation("io.ktor:ktor-client-okhttp-jvm:3.0.0-rc-2")

    // test
    testImplementation("org.jetbrains.kotlin:kotlin-test:" + properties["kotlinTestVersion"])
    testImplementation("org.mockito:mockito-core:" + properties["mockitoCoreVersion"])
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
}

tasks {
    val compileKotlinTask = named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin")
    compileKotlinTask.configure { dependsOn("generateEvents") }
}

tasks.build {
    dependsOn(tasks.test) // run tests before building
}
