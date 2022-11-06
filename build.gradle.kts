import java.net.URL
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.printer.PrettyPrinter
import com.github.javaparser.printer.PrettyPrinterConfiguration
import io.github.classgraph.ClassGraph
import java.io.FileWriter

buildscript {
    repositories { mavenCentral() }

    dependencies { classpath("org.jetbrains.dokka:dokka-base:1.7.20")
        classpath("io.github.classgraph:classgraph:4.8.149")

        classpath("com.github.javaparser:javaparser-core:3.24.7")

    }
}

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.allopen") version "1.7.20"
    id("com.diffplug.spotless") version "6.11.0"
    id("org.jetbrains.dokka") version "1.7.20"
    application
    `maven-publish`
    signing
    jacoco // code coverage reports
}

extra.apply {
    set("name", "YDWK")
    set("description", "YDWK (Yusuf's Discord Wrapper Kotlin) My own Discord Wrapper in Kotlin")
    set("group", "io.github.realyusufismail")
    set("version", "0.0.4")
    set("dev_id", "yusuf")
    set("dev_name", "Yusuf Ismail")
    set("dev_email", "yusufgamer222@gmail.com")
    set("dev_organization", "YDWK")
    set("dev_organization_url", "https://github.com/YDWK")
    set("gpl_name", "Apache-2.0 license")
    set("gpl_url", "https://github.com/YDWK/YDWK/blob/master/LICENSE")
}

group = "io.github.realyusufismail" // used for publishing. DON'T CHANGE

val releaseVersion by extra(!version.toString().endsWith("-SNAPSHOT"))

apply(from = "gradle/tasks/checkEntities.gradle.kts")

apply(from = "gradle/tasks/incrementVersion.gradle.kts")

apply(from = "gradle/tasks/checkEvents.gradle.kts")

//apply(from = "gradle/tasks/generateEvents.gradle.kts")

repositories { mavenCentral() }

dependencies {
    // json
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0-rc3")
    // logger
    api("ch.qos.logback:logback-classic:1.4.4")
    api("ch.qos.logback:logback-core:1.4.4")
    api("uk.org.lidalia:sysout-over-slf4j:1.0.2")
    // config.json
    api("io.github.realyusufismail:jconfig:1.0.7")
    // ws and https
    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    api("com.neovisionaries:nv-websocket-client:2.14")
    // kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.20")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "17" }

tasks.build {
    // dependsOn on custom tasks
    dependsOn(tasks.getByName("checkEvents"))
    dependsOn(tasks.getByName("checkEntities"))
    // dependsOn(tasks.getByName("generateEvents"))
}

tasks.jacocoTestReport {
    group = "Reporting"
    description = "Generate Jacoco coverage reports after running tests."
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    finalizedBy("jacocoTestCoverageVerification")
}

configurations { all { exclude(group = "org.slf4j", module = "slf4j-log4j12") } }

spotless {
    kotlin {
        // Excludes build folder since it contains generated java classes.
        targetExclude("build/**")
        ktfmt("0.39").dropboxStyle()

        licenseHeader(
            """/*
 * Copyright 2022 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ """)
    }

    kotlinGradle {
        target("**/*.gradle.kts")
        ktfmt("0.39").dropboxStyle()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

application { mainClass.set("MainKt") }

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

publishing {
    val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
    publications {
        create<MavenPublication>("ydwk") {
            from(components["java"])
            // artifactId = project.artifactId // or maybe archiveBaseName?
            pom {
                name.set(extra["name"] as String)
                description.set(extra["description"] as String)
                url.set("https://www.ydwk.org")
                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/YDWK/YDWK/issues")
                }
                licenses {
                    license {
                        name.set(extra["gpl_name"] as String)
                        url.set(extra["gpl_url"] as String)
                    }
                }
                ciManagement { system.set("GitHub Actions") }
                inceptionYear.set("2022")
                developers {
                    developer {
                        id.set(extra["dev_id"] as String)
                        name.set(extra["dev_name"] as String)
                        email.set(extra["dev_email"] as String)
                        organization.set(extra["dev_organization"] as String)
                        organizationUrl.set(extra["dev_organization_url"] as String)
                    }
                }
                scm {
                    connection.set("https://github.com/YDWK/YDWK.git")
                    developerConnection.set("scm:git:ssh://git@github.com/YDWK/YDWK.git")
                    url.set("github.com/YDWK/YDWK")
                }
            }
        }
    }
    repositories {
        maven {
            name = "MavenCentral"
            val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri((if (isReleaseVersion) releaseRepo else snapshotRepo))
            credentials {
                // try to get it from system gradle.properties
                println("Trying to get credentials from system properties")
                username =
                    when {
                        systemHasEnvVar("MAVEN_USERNAME") -> {
                            System.getenv("MAVEN_USERNAME")
                        }
                        project.hasProperty("MAVEN_USERNAME") -> {
                            project.property("MAVEN_USERNAME") as String
                        }
                        else -> {
                            logger.warn(
                                "MAVEN_USERNAME not found in system properties, meaning if you are trying to publish to maven central, it will fail")
                            null
                        }
                    }

                password =
                    when {
                        systemHasEnvVar("MAVEN_PASSWORD") -> {
                            System.getenv("MAVEN_PASSWORD")
                        }
                        project.hasProperty("MAVEN_PASSWORD") -> {
                            project.property("MAVEN_PASSWORD") as String
                        }
                        else -> {
                            logger.warn(
                                "MAVEN_PASSWORD not found in system properties, meaning if you are trying to publish to maven central, it will fail")
                            null
                        }
                    }
            }
        }
    }
}

fun systemHasEnvVar(varName: String): Boolean {
    return System.getenv(varName) != null
}

signing {
    afterEvaluate {
        // println "sign: " + isReleaseVersion
        val isRequired =
            releaseVersion &&
                (tasks.withType<PublishToMavenRepository>().find { gradle.taskGraph.hasTask(it) } !=
                    null)
        setRequired(isRequired)
        sign(publishing.publications["ydwk"])
    }
}

tasks.getByName("dokkaHtml", DokkaTask::class) {
    dokkaSourceSets.configureEach {
        includes.from("Package.md")
        jdkVersion.set(17)
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(
                URL("https://github.com/RealYusufIsmail/YDWK/tree/master/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright © 2022 Yusuf Arfan Ismail and other YDWK contributors."
        }
    }
}













//generate event 
val mainSrc = project(":")

tasks.create("generateEvents") {
    doLast {
        // search every interface that extends GenericEntity and look for variables with the
        // annotation @UpdatableVariable
        val allVariablesContaingUpdatableVariableAnotation:
                MutableList<io.github.classgraph.ClassInfo> =
            mutableListOf()

        val rawRootDir: String =
            mainSrc.projectDir.toString() + "/src/main/kotlin/io/github/ydwk/ydwk"
        val parsedRootDir: String = parseRootDir(rawRootDir)

        try {

            ClassGraph().acceptPackages(parsedRootDir).enableAllInfo().scan().use { scanResult ->
                val allGenericEntities =
                    scanResult.getClassesImplementing(
                        "io.github.ydwk.ydwk.entities.util.GenericEntity")
                allGenericEntities.forEach { genericEntity ->
                    val allFields = genericEntity.fieldAnnotations.annotations

                    allFields.forEach { field ->
                        if (field.name ==
                            "io.github.ydwk.ydwk.entities.util.annotations.UpdatableVariable") {
                            allVariablesContaingUpdatableVariableAnotation.add(genericEntity)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error getting all classes with UpdatableVariable annotation $e")
        }

        try {
            generateEvents(allVariablesContaingUpdatableVariableAnotation)
        } catch (e: Exception) {
            throw RuntimeException("Error generating events $e")
        }
    }
}

fun parseRootDir(rawRootDir: String): String {
    return rawRootDir.replace("\\", ".")
}

fun generateEvents(annotations: List<io.github.classgraph.ClassInfo>) {
    logger.lifecycle("Generating events...")

    // generate in the build folder
    val buildDir = project.buildDir.toString() + "/generated/events"

    // create the folder if it doesn't exist
    val buildDirFile = File(buildDir)
    if (buildDirFile.exists()) {
        buildDirFile.delete()
        try {
            buildDirFile.mkdirs()
        } catch (e: Exception) {
            throw RuntimeException("Error creating build directory $e")
        }
    } else {
        try {
            buildDirFile.mkdirs()
        } catch (e: Exception) {
            throw RuntimeException("Error creating build directory $e")
        }
    }

    try {
        createEventFile(buildDirFile)
    } catch (e: Exception) {
        throw RuntimeException("Error creating events file " + e.message)
    }

    try {
        logger.lifecycle("Creating event classes...")
        createEventClass(annotations, buildDirFile)
    } catch (e: Exception) {
        throw RuntimeException("Error creating event classes")
    }
}

fun createNewUpdateEvent(className: String, classDir: File) {
    logger.lifecycle("Creating ${className}UpdateEvent.kt...")

    val newUpdateEventFile = CompilationUnit("io.github.ydwk.ydwk.entities.events.${className}")

    newUpdateEventFile.addImport("io.github.ydwk.ydwk.entities.${className}")
    newUpdateEventFile.addImport("import io.github.ydwk.ydwk.evm.backend.event.IEventUpdate")
    val newUpdateEventClass =
        newUpdateEventFile
            .addClass(className + "UpdateEvent")
            .addExtends("IEventUpdate<${className}>")
}

fun createEventFile(buildDir: File) {
    val baseEventFile = CompilationUnit("io.github.ydwk.ydwk.events.Event.java")

    try {
        logger.lifecycle("Creating Event.kt...")
        baseEventFile.addImport("io.github.ydwk.ydwk.YDWK")
        baseEventFile.addImport("io.github.ydwk.ydwk.evm.backend.event.IEvent")

        val baseEventClass = baseEventFile.addClass("Event").addExtendedType("IEvent")

        baseEventFile.storage.ifPresent { it ->
            it.save {
                logger.lifecycle("Saving Event.kt...")
                PrettyPrinter(
                    PrettyPrinterConfiguration()
                        .setOrderImports(true)
                        .setEndOfLineCharacter("\n")
                        .setColumnAlignParameters(true)
                        .setColumnAlignFirstMethodChain(true))
                    .print(it)
            }
        }
            ?: throw RuntimeException("Error saving Event.kt")
    } catch (e: Exception) {
        throw RuntimeException("Error creating base event class $e")
    }
}

fun createEventClass(annotations: List<io.github.classgraph.ClassInfo>, eventsDir: File) {
    annotations.forEach { genericEntity ->
        // get the class name
        val className = genericEntity.simpleName

        // if a folder does not exist for that class name, create it
        val classDir = File(eventsDir, className)

        try {
            if (!classDir.exists()) {
                classDir.mkdir()
            }
        } catch (e: Exception) {
            throw RuntimeException("Error creating class directory")
        }

        // create a Generic + className + UpdateEvent.kt file with a generic T
        val genericUpdateEventFile = File(classDir, "Generic${className}UpdateEvent.kt")

        if (!genericUpdateEventFile.exists()) {
            genericUpdateEventFile.createNewFile()

            val genericUpdateEventFileWriter = FileWriter(genericUpdateEventFile)

            genericUpdateEventFileWriter.write("package io.github.ydwk.ydwk.events.${className}")
            genericUpdateEventFileWriter.write("\n")
            genericUpdateEventFileWriter.write("import io.github.ydwk.ydwk.YDWK")
            genericUpdateEventFileWriter.write("import io.github.ydwk.ydwk.entities.${className}")
            genericUpdateEventFileWriter.write(
                "import io.github.ydwk.ydwk.evm.backend.event.IEventUpdate")
            genericUpdateEventFileWriter.write("\n")
            genericUpdateEventFileWriter.write(
                """
                    open class Generic${className}UpdateEvent<T>(
                        override val ydwk: YDWK,
                        override val entity: ${className},
                        override val oldValue: T,
                        override val newValue: T
                    ) : IEventUpdate<${className}, T>
                """.trimIndent())
        }

        logger.lifecycle("Created Generic${className}UpdateEvent.kt")
        // create new event file
        createNewUpdateEvent(className, classDir)
    }
}

class EventFileWriter(private val eventFile: File) {
    private val fileWriter: FileWriter = FileWriter(eventFile)

    fun write(s: String) {
        fileWriter.write(s)
    }

    fun close() {
        fileWriter.close()
    }
}
