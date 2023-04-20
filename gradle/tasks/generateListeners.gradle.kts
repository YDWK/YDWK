import com.squareup.kotlinpoet.*
import io.github.classgraph.*
import java.util.*
import io.github.ydwk.ydwk.*
import io.github.ydwk.ydwk.evm.listener.extendable.*

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
        classpath("io.github.classgraph:classgraph:" + properties["classGraphVersion"])
        //add buildSrc dependencies from root build.gradle.kts
        //classpath("io.github.ydwk.ydwk:buildSrc:" + properties["buildSrcVersion"])
    }
}

tasks.register("generateListeners") {
    group = "ydwk"
    description = "Generate listeners"

    doLast {
        val classGraph = ClassGraph()
            .enableAllInfo()
            .whitelistPackages("io.github.ydwk.ydwk")
            .scan()

        val coreListeners =
            classGraph.getClassesImplementing(ExtendableCoreListener::class.java.name)

        val guildListeners =
            classGraph.getClassesImplementing(ExtendableGuildListener::class.java.name)

        val channelListeners =
            classGraph.getClassesImplementing(ExtendableChannelListener::class.java.name)

        val userListeners =
            classGraph.getClassesImplementing(ExtendableUserListener::class.java.name)

        val voiceListeners =
            classGraph.getClassesImplementing(ExtendableVoiceListener::class.java.name)

        val interactionListeners
            classGraph.getClassesImplementing(ExtendableInteractionListener::class.java.name)

        val guildModerationListeners =
            classGraph.getClassesImplementing(ExtendableGuildModerationListener::class.java.name)
    }
}
