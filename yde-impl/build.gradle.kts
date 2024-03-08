plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    application
    `maven-publish`
}

apply(from = "gradle/tasks/GenerateRestMethodImpl.gradle.kts")

extra.apply {
    set("name", "YDE Impl")
    set("description", "YDE (Yusuf's Discord Entities) Impl")
    set("dev_id", "yusuf")
    set("dev_name", "Yusuf Ismail")
    set("dev_email", "yusufgamer222@gmail.com")
    set("dev_organization", "YDWK")
    set("dev_organization_url", "https://github.com/YDWK")
    set("gpl_name", "Apache-2.0 license")
    set("gpl_url", "https://github.com/YDWK/YDWK/blob/master/LICENSE")
}

dependencies { api(project(":yde-api")) }

tasks {
    val compileKotlinTask = named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin")
    compileKotlinTask.configure { dependsOn("generateRestAPIMethodGettersImpls") }
}
