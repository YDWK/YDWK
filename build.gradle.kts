import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
    }
}

plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
    id("com.github.ben-manes.versions")
    application
    `maven-publish`
    signing
    jacoco // code coverage reports
}

group = "io.github.realyusufismail" // used for publishing. DON'T CHANGE

val releaseVersion by extra(!version.toString().endsWith("-SNAPSHOT"))

apply(from = "gradle/tasks/incrementVersion.gradle.kts")

allprojects {
    repositories { mavenCentral() }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    jvmArgs("--enable-preview")
}

tasks.build {
    dependsOn(tasks.test) // run tests before building

    // check if version is not snapshot
    if (releaseVersion) {
        // check if MAVEN_PASSWORD is set
        if (System.getenv("mavenToken") != null) {
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

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "jacoco")
    apply(plugin = "com.diffplug.spotless")

    group = "io.github.realyusufismail" // used for publishing. DON'T CHANGE

    kotlin {
        compilerOptions {
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.DEFAULT)
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.DEFAULT)

            java.targetCompatibility = JavaVersion.VERSION_21
            java.sourceCompatibility = JavaVersion.VERSION_21
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> { options.compilerArgs.add("--enable-preview") }

    tasks.withType<JavaExec> { jvmArgs("--enable-preview") }

    spotless {
        kotlin {
            // Excludes build folder since it contains generated java classes.
            targetExclude("build/**")
            ktfmt("0.52").dropboxStyle()

            licenseHeader(
                """/*
 * Copyright 2024-2026 YDWK inc.
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
            ktfmt("0.52").dropboxStyle()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    tasks.jar {
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to (project.extra.properties["dev_organization"] ?: ""),
                "Implementation-Vendor-Id" to (project.extra.properties["dev_id"] ?: ""),
                "Implementation-Vendor-Name" to (project.extra.properties["dev_name"] ?: ""),
                "Implementation-Vendor-Email" to (project.extra.properties["dev_email"] ?: ""),
                "Implementation-Vendor-Organization" to (project.extra.properties["dev_organization"] ?: ""),
                "Implementation-Vendor-Organization-Url" to (project.extra.properties["dev_organization_url"] ?: ""),
                "Implementation-License" to (project.extra.properties["gpl_name"] ?: ""),
                "Implementation-License-Url" to (project.extra.properties["gpl_url"] ?: ""),
            )
        }
    }

    tasks.javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }


    publishing {
        val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")
        publications {
            create<MavenPublication>(project.name) {
                val developerInfo = DeveloperInfo(
                    id = project.extra["dev_id"] as String,
                    name = project.extra["dev_name"] as String,
                    email = project.extra["dev_email"] as String,
                    organization = project.extra["dev_organization"] as String,
                    organizationUrl = project.extra["dev_organization_url"] as String
                )

                val licenseInfo = LicenseInfo(
                    name = project.extra["gpl_name"] as String,
                    url = project.extra["gpl_url"] as String
                )

                val ciInfo = "GitHub Actions"
                val issueManagementInfo = IssueManagementInfo(system = "GitHub", url = "https://github.com/YDWK/YDWK/issues")

                from(components["java"])
                version = project.version.toString()

                setupDeveloperInfo(developerInfo)
                setupLicenseInfo(licenseInfo)
                setupCiManagement(ciInfo)
                setupIssueManagement(issueManagementInfo)

                pom {
                    name.set(project.name)
                    description.set(project.description ?: "No description provided")
                    url.set("https://github.com/YDWK/YDWK")
                    scm {
                        connection.set("https://github.com/YDWK/YDWK.git")
                        developerConnection.set("scm:git:ssh://git@github.com/YDWK/YDWK.git")
                        url.set("https://github.com/YDWK/YDWK/")
                    }
                }
            }
        }

        repositories {
            // Sonatype Central Portal (OSSRH was shut down June 2025)
            // For automated publishing, use the com.gradleup.nmcp plugin with Central Portal.
            // The credentials below map to your Central Portal user token (username/password).
            maven {
                name = "MavenCentral"
                val releaseRepo = "https://central.sonatype.com/api/v1/publisher/upload"
                val snapshotRepo = "https://central.sonatype.com/api/v1/publisher/upload"
                url = uri(if (isReleaseVersion) releaseRepo else snapshotRepo)

                credentials {
                    username = project.findProperty("mavenUsername") as String? ?: run {
                        println("mavenUsername not found, publishing will fail")
                        null
                    }

                    password = project.findProperty("mavenToken") as String? ?: run {
                        println("mavenToken not found, publishing will fail")
                        null
                    }
                }
            }
        }
    }


    signing {
        afterEvaluate {
            // println "sign: " + isReleaseVersion
            val isRequired =
                releaseVersion &&
                        (tasks.withType<PublishToMavenRepository>().find { gradle.taskGraph.hasTask(it) } !=
                                null)
            setRequired(isRequired)
            sign(publishing.publications[project.name])
        }
    }


    sourceSets { main { kotlin { srcDirs("src/main/kotlin", "build/generated/kotlin") } } }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    group = "dependency updates"

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "plain"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("Package.md")
        jdkVersion.set(21)
        sourceLink {
            localDirectory.set(file("ydwk/src/main/kotlin"))
            remoteUrl("https://github.com/YDWK/YDWK/tree/master/src/main/kotlin")
            remoteLineSuffix.set("#L")
        }
    }
    pluginsConfiguration.html {
        footerMessage = "Copyright © 2024-2026 YDWK inc."
    }
}

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

data class DeveloperInfo(
    val id: String,
    val name: String,
    val email: String,
    val organization: String,
    val organizationUrl: String
)

data class LicenseInfo(val name: String, val url: String)

data class IssueManagementInfo(val system: String, val url: String)

fun systemHasEnvVar(varName: String): Boolean {
    return System.getenv(varName) != null
}
