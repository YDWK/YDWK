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


group = "io.github.realyusufismail" // used for publishing. DON'T CHANGE

val releaseVersion by extra(!version.toString().endsWith("-SNAPSHOT"))

apply(from = "gradle/tasks/incrementVersion.gradle.kts")

apply(from = "gradle/tasks/Nexus.gradle")

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

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "jacoco")
    apply(plugin = "com.diffplug.spotless")

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

    tasks.jar {
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to project.extra.properties["dev_organization"],
                "Implementation-Vendor-Id" to project.extra.properties["dev_id"],
                "Implementation-Vendor-Name" to project.extra.properties["dev_name"],
                "Implementation-Vendor-Email" to project.extra.properties["dev_email"],
                "Implementation-Vendor-Organization" to project.extra.properties["dev_organization"],
                "Implementation-Vendor-Organization-Url" to project.extra.properties["dev_organization_url"],
                "Implementation-License" to project.extra.properties["gpl_name"],
                "Implementation-License-Url" to project.extra.properties["gpl_url"],
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
                project.extra.properties.forEach { (k, v) -> logger.debug("{}: {}", k, v) }

                val developerInfo =
                    DeveloperInfo(
                        id =  project.extra.properties["dev_id"] as String,
                        name = project.extra.properties["dev_name"] as String,
                        email = project.extra.properties["dev_email"] as String,
                        organization = project.extra.properties["dev_organization"] as String,
                        organizationUrl = project.extra.properties["dev_organization_url"] as String)

                val licenseInfo = LicenseInfo(name = project.extra.properties["gpl_name"] as String, url = project.extra.properties["gpl_url"] as String)

                val ciInfo = "GitHub Actions"

                val issueManagementInfo =
                    IssueManagementInfo(system = "GitHub", url = "https://github.com/YDWK/YDWK/issues")


                from(components["java"])
                version = project.version.toString()
                setupDeveloperInfo(developerInfo)
                setupLicenseInfo(licenseInfo)
                setupCiManagement(ciInfo)
                setupIssueManagement(issueManagementInfo)
                pom {
                    name.set(project.name)
                    description.set(project.description)

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
data class DeveloperInfo(
    val id: String,
    val name: String,
    val email: String,
    val organization: String,
    val organizationUrl: String
)

data class LicenseInfo(val name: String, val url: String)

data class IssueManagementInfo(val system: String, val url: String)


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

fun systemHasEnvVar(varName: String): Boolean {
    return System.getenv(varName) != null
}


tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    group = "dependency updates"

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "plain"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

tasks.getByName("dokkaHtml", DokkaTask::class) {
    dokkaSourceSets.configureEach {
        includes.from("Package.md")
        jdkVersion.set(21)
        sourceLink {
            localDirectory.set(file("ydwk/src/main/kotlin"))
            remoteUrl.set(URL("https://github.com/YDWK/YDWK/tree/master/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright © 2024 YDWK inc."
        }
    }
}

