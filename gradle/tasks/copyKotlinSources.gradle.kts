tasks.register("copyKotlinSources") {
    group = "generate"
    description = "Copies Kotlin source files from subprojects/ to buildSrc/"

    inputs.dir("ydwk-core")
    inputs.dir("ydwk-event-manager")
    outputs.dir("buildSrc")

    // add gradlw files
    val gradlewFolder = copy {
        from("gradle")
        into("buildSrc/gradle")
    }

    copy {
        from("ydwk-core") { include("**/*.kt") }
        into("buildSrc/ydwk-core")
    }
    copy {
        from("ydwk-event-manager") { include("**/*.kt") }
        into("buildSrc/ydwk-event")
    }

    doLast {
        val gradlePropertiesFileForEventManager = copy {
            from("ydwk-event-manager/gradle.properties")
            into("buildSrc/ydwk-event")
        }

        val buildFileForEventManager = copy {
            from("ydwk-event-manager/build.gradle.kts")
            into("buildSrc/ydwk-event")
        }

        val gradlePropertiesFileForCore = copy {
            from("ydwk-core/gradle.properties")
            into("buildSrc/ydwk-core")
        }

        val buildFileForCore = copy {
            from("ydwk-core/build.gradle.kts")
            into("buildSrc/ydwk-core")
        }

        val mainBuildFile = copy {
            from("build.gradle.kts")
            into("buildSrc")
        }

        val gradlePropertiesFile = copy {
            from("gradle.properties")
            into("buildSrc")
        }

        val gradleSettingsFile = copy {
            from("settings.gradle.kts")
            into("buildSrc")
        }
    }
}
