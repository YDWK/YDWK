import com.squareup.kotlinpoet.*
import io.github.classgraph.ClassGraph
import java.util.*

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
        classpath("io.github.classgraph:classgraph:" + properties["classGraphVersion"])
    }
}

tasks.register("generate") {
    doLast {
        val classGraph = ClassGraph()
            .enableAllInfo()
            .scan()

        // two types of listeners.
        // your normals ones such as CoreListeners 
        // the other yde.coreListeners.onReadyEvent which is a special one

        val coreListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableCoreListener")
        val guildListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildListener")
        val channelListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableChannelListener")
        val userListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableUserListener")
        val voiceListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableVoiceListener")
        val interactionListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableInteractionListener")
        val guildModerationListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildModerationListener")

        // two files to write to
        // one has an interface for each listener type (core, guild, channel, etc) and it extends IEventListener. At the end of the file it will overide onEvent(event : GenericEvent) and call the correct event (onReadyEvent, onGuildJoinEvent, etc
        // the other will events.kt which will have events such as onReadyEvent
        // in this format 
        // inline fun onReadyEvent(crossinline block: suspend (ReadyEvent) -> Unit) {
        //     coreListeners.onReadyEvent(block)
        // }
        // and the same for all the other events

        //extend the IEventListener interface
        val coreListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "CoreListeners").addType(
            TypeSpec.interfaceBuilder("CoreListeners")
                .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
                
        val guildListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "GuildListeners")
            .addType(
                TypeSpec.interfaceBuilder("GuildListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )
            
        val channelListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "ChannelListeners")
            .addType(
                TypeSpec.interfaceBuilder("ChannelListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )

        val userListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "UserListeners")
            .addType(
                TypeSpec.interfaceBuilder("UserListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )
            
        val voiceListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "VoiceListeners")
            .addType(
                TypeSpec.interfaceBuilder("VoiceListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )
            
        val interactionListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "InteractionListeners")
            .addType(
                TypeSpec.interfaceBuilder("InteractionListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )
            
        val guildModerationListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "GuildModerationListeners")
            .addType(
                TypeSpec.interfaceBuilder("GuildModerationListeners")
                    .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
            )

        val eventFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "Events")   
    }
}