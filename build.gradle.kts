import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.10"
    application
}

group = "io.github.realyusufismail"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //json
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    //logger
    implementation("org.apache.logging.log4j:log4j-core:2.18.0")
    implementation("org.apache.logging.log4j:log4j-api:2.18.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.18.0")
    //.env
    implementation("io.github.yusufsdiscordbot:config:1.0.4")
    //ws and https
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.neovisionaries:nv-websocket-client:2.14")
    //other
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}