import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.net.URL
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.10")
        classpath("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.30.0")
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    // id("io.gitlab.arturbosch.detekt")
    id("com.github.ben-manes.versions")
    application
    `maven-publish`
    signing
    jacoco // code coverage reports
}

apply(plugin = "io.codearte.nexus-staging")

extra.apply {
    set("name", "YDWK")
    set("description", "YDWK (Yusuf's Discord Wrapper Kotlin) My own Discord Wrapper in Kotlin")
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

apply(from = "gradle/tasks/incrementVersion.gradle.kts")

apply(from = "gradle/tasks/checkEvents.gradle.kts")

apply(from = "gradle/tasks/eventClassJavaDocChecker.gradle")

apply(from = "gradle/tasks/Nexus.gradle")

apply(from = "gradle/tasks/GenerateKotlinCode.gradle.kts")

apply(from = "gradle/tasks/generateEvents.gradle.kts")

repositories { mavenCentral() }

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
    api("com.neovisionaries:nv-websocket-client:" + properties["nvWebsocketClientVersion"])
    api("com.codahale:xsalsa20poly1305:" + properties["xsalsa20poly1305Version"])
    api("io.ktor:ktor-client-core-jvm:" + properties["ktorClientCoreJvmVersion"])

    // kotlin
    api(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:" +
            properties["kotlinxCoroutinesCoreVersion"])

    // annotations
    implementation("com.google.code.findbugs:jsr305:" + properties["jsr305Version"])

    // decode Opus
    api("org.jitsi:libjitsi:" + properties["libjitsiVersion"])

    // files to bytes
    api("commons-io:commons-io:" + properties["commonsIoVersion"])

    // guava
    api("com.google.guava:guava:" + properties["guavaVersion"])

    // test
    testImplementation("org.jetbrains.kotlin:kotlin-test:" + properties["kotlinTestVersion"])
    testImplementation("org.mockito:mockito-core:" + properties["mockitoCoreVersion"])
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    jvmArgs("--enable-preview")
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "21" }

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach { jvmTarget = "21" }

// tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
// jvmTarget = "21"
// }

tasks.withType<JavaCompile> { options.compilerArgs.add("--enable-preview") }

tasks.withType<JavaExec> { jvmArgs("--enable-preview") }

tasks {
    val compileKotlinTask = named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin")
    compileKotlinTask.configure { dependsOn("generateEvents") }
}

tasks.build {
    // dependsOn on custom tasks
    dependsOn(tasks.getByName("checkEvents")) // check if events are valid
    dependsOn(tasks.getByName("eventClassJavaDocChecker")) // check if event classes have javadoc
    dependsOn(tasks.test) // run tests before building

    // check if version is not snapshot
    if (releaseVersion) {
        // check if MAVEN_PASSWORD is set
        if (System.getenv("MAVEN_PASSWORD") != null) {
            // run publishYdwkPublicationToMavenCentralRepository
            dependsOn(tasks.getByName("publishYdwkPublicationToMavenCentralRepository"))
            // then increment version
            dependsOn(tasks.getByName("incrementVersion"))
        } else {
            // ignore
        }
    }
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
        ktfmt("0.42").dropboxStyle()

        licenseHeader(
            """/*
 * Copyright 2024 YDWK inc.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
        ktfmt("0.42").dropboxStyle()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

/*
detekt {
    // only check javadoc in io/github/ydwk/ydwk/entities and io/github/ydwk/ydwk/evm/event/events
    source.from(files("src/main/kotlin/io/github/ydwk/ydwk/evm/event/events"))
    config.from(files("gradle/config/detekt.yml"))
    baseline = file("gradle/config/detekt-baseline.xml")
    allRules = false
}

 */

// tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
//  reports {
//    xml.required.set(true)
//  html.required.set(true)
// }
// }

application { mainClass.set("MainKt") }

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to project.extra["dev_organization"],
            "Implementation-Vendor-Id" to project.extra["dev_id"],
            "Implementation-Vendor-Name" to project.extra["dev_name"],
            "Implementation-Vendor-Email" to project.extra["dev_email"],
            "Implementation-Vendor-Organization" to project.extra["dev_organization"],
            "Implementation-Vendor-Organization-Url" to project.extra["dev_organization_url"],
            "Implementation-License" to project.extra["gpl_name"],
            "Implementation-License-Url" to project.extra["gpl_url"],
        )
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    group = "dependency updates"

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "plain"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

val developerInfo =
    DeveloperInfo(
        id = extra["dev_id"] as String,
        name = extra["dev_name"] as String,
        email = extra["dev_email"] as String,
        organization = extra["dev_organization"] as String,
        organizationUrl = extra["dev_organization_url"] as String)

val licenseInfo = LicenseInfo(name = extra["gpl_name"] as String, url = extra["gpl_url"] as String)

val ciInfo = "GitHub Actions"

val issueManagementInfo =
    IssueManagementInfo(system = "GitHub", url = "https://github.com/YDWK/YDWK/issues")

publishing {
    val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
    publications {
        create<MavenPublication>("ydwk") {
            from(components["java"])
            version = project.version.toString()
            setupDeveloperInfo(developerInfo)
            setupLicenseInfo(licenseInfo)
            setupCiManagement(ciInfo)
            setupIssueManagement(issueManagementInfo)
            pom {
                scm {
                    connection.set("https://github.com/YDWK/YDWK.git")
                    developerConnection.set("scm:git:ssh://git@github.com/YDWK/YDWK.git")
                    url.set("github.com/YDWK/YDWK/")
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
                logger.debug("Trying to get credentials from system gradle.properties")
                username =
                    when {
                        systemHasEnvVar("MAVEN_USERNAME") -> {
                            logger.debug("Found username in system gradle.properties")
                            System.getenv("MAVEN_USERNAME")
                        }
                        project.hasProperty("MAVEN_USERNAME") -> {
                            logger.debug("MAVEN_USERNAME found in gradle.properties")
                            project.property("MAVEN_USERNAME") as String
                        }
                        else -> {
                            logger.debug(
                                "MAVEN_USERNAME not found in system properties, meaning if you are trying to publish to maven central, it will fail")
                            null
                        }
                    }

                password =
                    when {
                        systemHasEnvVar("MAVEN_PASSWORD") -> {
                            logger.debug("Found password in system gradle.properties")
                            System.getenv("MAVEN_PASSWORD")
                        }
                        project.hasProperty("MAVEN_PASSWORD") -> {
                            logger.debug("MAVEN_PASSWORD found in gradle.properties")
                            project.property("MAVEN_PASSWORD") as String
                        }
                        else -> {
                            logger.debug(
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
        jdkVersion.set(21)
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(URL("https://github.com/YDWK/YDWK/tree/master/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright © 2024 YDWK inc."
        }
    }
}

sourceSets { main { kotlin { srcDirs("src/main/kotlin", "build/generated/kotlin") } } }

fun MavenPublication.setupDeveloperInfo(developerInfo: DeveloperInfo) {
    pom {
        developers {
            developer {
                id.set(developerInfo.id)
                name.set(developerInfo.name)
                email.set(developerInfo.email)
                organization.set(developerInfo.organization)
                organizationUrl.set(developerInfo.organizationUrl)
            }
        }
    }
}

fun MavenPublication.setupLicenseInfo(licenseInfo: LicenseInfo) {
    pom {
        licenses {
            license {
                name.set(licenseInfo.name)
                url.set(licenseInfo.url)
            }
        }
    }
}

fun MavenPublication.setupCiManagement(ciInfo: String) {
    pom { ciManagement { system.set(ciInfo) } }
}

fun MavenPublication.setupIssueManagement(issueManagementInfo: IssueManagementInfo) {
    pom {
        issueManagement {
            system.set(issueManagementInfo.system)
            url.set(issueManagementInfo.url)
        }
    }
}

data class LicenseInfo(val name: String, val url: String)

data class IssueManagementInfo(val system: String, val url: String)

data class DeveloperInfo(
    val id: String,
    val name: String,
    val email: String,
    val organization: String,
    val organizationUrl: String
)
