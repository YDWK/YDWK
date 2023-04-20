import com.squareup.kotlinpoet.*
import io.github.classgraph.*
import java.util.*

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
        classpath("io.github.classgraph:classgraph:" + properties["classGraphVersion"])
    }
}

tasks.register("generateListeners") {
    doLast {
        val classGraph = ClassGraph()
            .enableAllInfo()
            .scan()

        val coreListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableCoreListener")
        val guildListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildListener")
        val channelListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableChannelListener")
        val userListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableUserListener")
        val voiceListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableVoiceListener")
        val interactionListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableInteractionListener")
        val guildModerationListeners = classGraph.getClassesImplementing("io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildModerationListener")

        val coreListenerFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "CoreListeners").addType(
            TypeSpec.interfaceBuilder("CoreListeners")
                .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event.IEventListener", "IEventListener"))
        )
                
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

        addFunctions(coreListenerFile, coreListeners)
        addFunctions(guildListenerFile, guildListeners)
        addFunctions(channelListenerFile, channelListeners)
        addFunctions(userListenerFile, userListeners)
        addFunctions(voiceListenerFile, voiceListeners)
        addFunctions(interactionListenerFile, interactionListeners)
        addFunctions(guildModerationListenerFile, guildModerationListeners)
        
        addOverideOnEvent(coreListenerFile, coreListeners)
        addOverideOnEvent(guildListenerFile, guildListeners)
        addOverideOnEvent(channelListenerFile, channelListeners)
        addOverideOnEvent(userListenerFile, userListeners)
        addOverideOnEvent(voiceListenerFile, voiceListeners)
        addOverideOnEvent(interactionListenerFile, interactionListeners)
        addOverideOnEvent(guildModerationListenerFile, guildModerationListeners)

        addInlineEventsToEventFile(eventFile, coreListeners)
        addInlineEventsToEventFile(eventFile, guildListeners)
        addInlineEventsToEventFile(eventFile, channelListeners)
        addInlineEventsToEventFile(eventFile, userListeners)
        addInlineEventsToEventFile(eventFile, voiceListeners)
        addInlineEventsToEventFile(eventFile, interactionListeners)
        addInlineEventsToEventFile(eventFile, guildModerationListeners)
    

         File("${project.buildDir}/generated/kotlin").apply {
            mkdirs()
            coreListenerFile.build().writeTo(this)
            guildListenerFile.build().writeTo(this)
            channelListenerFile.build().writeTo(this)
            userListenerFile.build().writeTo(this)
            voiceListenerFile.build().writeTo(this)
            interactionListenerFile.build().writeTo(this)
            guildModerationListenerFile.build().writeTo(this)

            eventFile.build().writeTo(this)
         }
    }
}

fun addFunctions(file: FileSpec.Builder, listeners: List<ClassInfo>) {
    listeners.forEach { listener ->
        //get the name of the class and add "on" to the front
        val name = listener.simpleName
        // the paramater in the function is just the event class name but with a lowercase first letter
        val eventParam = listener.simpleName.first().toLowerCase() + listener.simpleName.substring(1)
        val eventName = "on" + name
        val eventPackage = listener.packageName

        //add the function to the interface
        file.addType(
            TypeSpec.interfaceBuilder(name)
                .addFunction(
                    FunSpec.builder(eventName)
                        .addParameter(eventParam, ClassName(eventPackage, name))
                        .build()
                )
                .build()
        )
    }
}

fun addOverideOnEvent(file: FileSpec.Builder, listeners: List<ClassInfo>) {
    listeners.forEach { listener ->
        //get the name of the class and add "on" to the front
        val name = listener.simpleName
        // the paramater in the function is just the event class name but with a lowercase first letter
        val eventParam = listener.simpleName.first().toLowerCase() + listener.simpleName.substring(1)
        val eventName = "on" + name
        val eventPackage = listener.packageName
        
        file.addFunction(
            FunSpec.builder("onEvent")
                .addParameter("event", ClassName("io.github.ydwk.ydwk.evm.backend.event.", "GenericEvent"))
                .addCode(
                    CodeBlock.builder()
                        .beginControlFlow("when (event)")
                        .addStatement("is %T -> %L(event)", ClassName(eventPackage, name), eventName)
                        .endControlFlow()
                        .build()
                )
                .build()
        )
    }
}

fun addInlineEventsToEventFile(file: FileSpec.Builder, listeners: List<ClassInfo> ) {
    listeners.forEach { listener ->
        //get the name of the class and add "on" to the front
        val name = listener.simpleName
        // the paramater in the function is just the event class name but with a lowercase first letter
        val eventParam = listener.simpleName.first().toLowerCase() + listener.simpleName.substring(1)
        val eventName = "on" + name
        val eventPackage = listener.packageName
        
        file.addFunction(
            FunSpec.builder("on${name}")
                .addParameter("consumer", LambdaTypeName.get(
                    parameters = listOf(
                        ParameterSpec.builder("event", ClassName(eventPackage, name)).build()
                    ),
                    returnType = UNIT
                ))
                .returns(ClassName("io.github.ydwk.ydwk.evm.backend.event", "CoroutineEventListener"))
                .addCode(
                    CodeBlock.builder()
                        .addStatement("return object : CoroutineEventListener {")
                        .addStatement("override suspend fun onEvent(event: GenericEvent) {")
                        .addStatement("if (event is %T) {", ClassName(eventPackage, name))
                        .addStatement("event.consumer(event)")
                        .addStatement("}")
                        .addStatement("}")
                        .addStatement("}")
                        .addStatement(".also { this.addEventListeners(it) }")
                        .build()
                )
                .build()
        )
    }
}