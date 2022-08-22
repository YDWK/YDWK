import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.allopen") version "1.7.10"
    id("com.diffplug.spotless") version "6.9.1"
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
    set("gpl_name", "GNU Library General Public License version 3")
    set("gpl_url", "https://github.com/RealYusufIsmail/YDWK/blob/master/LICENSE")
    // Make sure we have a default for initial configuration evaluation
    set("isReleaseVersion", "false")
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
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha16")
    implementation("ch.qos.logback:logback-core:1.3.0-beta0")
    implementation("uk.org.lidalia:sysout-over-slf4j:1.0.2")
    //.env
    implementation("io.github.yusufsdiscordbot:config:1.0.4")
    //ws and https
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    implementation("com.neovisionaries:nv-websocket-client:2.14")
    //other
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.7.10")
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
                url.set("https://github.com/RealYusufIsmail/YDW-Kotlin")
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
                    connection.set("https://github.com/RealYusufIsmail/YDW-Kotlin.git")
                    developerConnection.set("scm:git:ssh://git@github.com/RealYusufIsmail/YDW-Kotlin.git")
                    url.set("github.com/RealYusufIsmail/YDW-Kotlin")
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
                    val username =
                        if (project.hasProperty("ossrhUsername")) project.property("ossrhUsername") as String else "Unknown user"
                    val password =
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
