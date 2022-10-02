import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.7.10")
    }
}

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.allopen") version "1.7.10"
    id("com.diffplug.spotless") version "6.11.0"
    id("org.jetbrains.dokka") version "1.7.10"
    application
    `maven-publish`
    signing
    jacoco  // code coverage reports
}


extra.apply {
    set("name", "YDWK")
    set("description", "YDWK (Yusuf's Discord Wrapper Kotlin) My own Discord Wrapper in Kotlin")
    set("group", "io.github.realyusufismail")
    set("version", "0.0.1")
    set("dev_id", "yusuf")
    set("dev_name", "Yusuf Ismail")
    set("dev_email", "yusufgamer222@gmail.com")
    set("dev_organization", "RealYusufIsmail")
    set("dev_organization_url", "https://github.com/RealYusufIsmail")
    set("gpl_name", "Apache-2.0 license")
    set("gpl_url", "https://github.com/RealYusufIsmail/YDWK/blob/master/LICENSE")
    // Make sure we have a default for initial configuration evaluation
    set("isReleaseVersion", false)
}

group = "io.github.realyusufismail"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //json
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    //logger
    api("ch.qos.logback:logback-classic:1.4.1")
    api("ch.qos.logback:logback-core:1.4.1")
    api("uk.org.lidalia:sysout-over-slf4j:1.0.2")
    //.env
    api("io.github.yusufsdiscordbot:config:1.0.4")
    //ws and https
    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    api("com.neovisionaries:nv-websocket-client:2.14")
    //kotlin
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
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

configurations {
    all {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
}

spotless {
    kotlin {
        // Excludes build folder since it contains generated java classes.
        targetExclude("build/**")
        ktfmt("0.39").dropboxStyle()

        licenseHeader(
            """/*
 * Copyright 2022 Yusuf Arfan Ismail and other YDWK contributors.
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
 */ """
        )
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}

java {
    withJavadocJar()
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

afterEvaluate {
    val version = extra["version"] as String
    extra["isReleaseVersion"] = !version.endsWith("RELEASE")
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            //artifactId = project.artifactId // or maybe archiveBaseName?
            pom {
                name.set(extra["name"] as String)
                description.set(extra["description"] as String)
                url.set("https://github.com/RealYusufIsmail/YDWK")
                licenses {
                    license {
                        name.set(extra["gpl_name"] as String)
                        url.set(extra["gpl_url"] as String)
                    }
                }
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
                    connection.set("https://github.com/RealYusufIsmail/YDWK.git")
                    developerConnection.set("scm:git:ssh://git@github.com/RealYusufIsmail/YDWK.git")
                    url.set("github.com/RealYusufIsmail/YDWK")
                }
            }
        }
    }
    repositories {
        maven {
            afterEvaluate {
                val releaseRepo = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotRepo = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri((if (extra["isReleaseVersion"] == true) releaseRepo else snapshotRepo))

                //println "repos: " + version
                //println "repos: " + isReleaseVersion
                //println url

                credentials {
                    //change roject.hasProperty('ossrhUsername') ? ossrhUsername : "Unknown user" to kotlin

                    //: Type mismatch: inferred type is Any but String? was expected
                    username =
                        if (project.hasProperty("ossrhUsername")) project.property("ossrhUsername") as String else "Unknown user"
                    password =
                        if (project.hasProperty("ossrhPassword")) project.property("ossrhPassword") as String else "Unknown password"
                }
            }
        }
    }
}

signing {
    afterEvaluate {
        //println "sign: " + version
        //println "sign: " + isReleaseVersion
        val required = extra["isReleaseVersion"] as Boolean && gradle.taskGraph.hasTask("publish")
        sign(publishing.publications["mavenJava"])
    }
}

tasks.getByName("dokkaHtml", DokkaTask::class) {
    dokkaSourceSets.configureEach {
        includes.from("Package.md")
        jdkVersion.set(17)
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(URL("https://github.com/RealYusufIsmail/YDWK/tree/master/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "Copyright © 2022 Yusuf Arfan Ismail and other YDWK contributors."
        }
    }
}

