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
    try {
        var classGraph: ScanResult? = null
        try {
            classGraph = ClassGraph().enableAllInfo().scan()
        } catch (e: Exception) {
            e.printStackTrace()
            logger.lifecycle("Failed to scan classpath")
        }

        if (classGraph == null) {
            logger.lifecycle("Failed to scan classpath")
            throw Exception("Failed to scan classpath")
        } else {

            logger.lifecycle("Generating listeners")

           val coreListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableCoreListener")
            val guildListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildListener")
            val channelListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableChannelListener")
            val userListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableUserListener")
            val voiceListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableVoiceListener")
            val interactionListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableInteractionListener")
            val guildModerationListeners =
                classGraph.getClassesImplementing(
                    "io.github.ydwk.ydwk.evm.listeners.extendable.ExtendableGuildModerationListener")

            logger.lifecycle("Found ${coreListeners.size} core listeners")

            val coreListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "CoreListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("CoreListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                coreListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            )//add overmide method
                            .addFunction(
                                FunSpec.builder("onEvent")
                                    .addModifiers(KModifier.OVERRIDE)
                                    .addParameter(
                                        ParameterSpec.builder(
                                            "event",
                                            ClassName(
                                                "io.github.ydwk.ydwk.evm.backend.event",
                                                "Event"
                                            )
                                        ).build()
                                    )
                                    .addStatement(
                                        "when(event) {" +
                                                coreListeners.forEach {
                                                    "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                                }.joinToString("\n") +
                                                "}"
                                    )
                                    .build()
                            )
                            )
                            .build()

            val guildListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "GuildListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("GuildListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                guildListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            ).addFunction(
                                FunSpec.builder("onEvent")
                                    .addModifiers(KModifier.OVERRIDE)
                                    .addParameter(
                                        ParameterSpec.builder(
                                            "event",
                                            ClassName(
                                                "io.github.ydwk.ydwk.evm.backend.event",
                                                "Event"
                                            )
                                        ).build()
                                    )
                                    .addStatement(
                                        "when(event) {" +
                                                guildListeners.forEach {
                                                    "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                                }.joinToString("\n") +
                                                "}"
                                    )
                                    .build()
                            )
                    ).build()

            val channelListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "ChannelListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("ChannelListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                channelListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            )
                    .addFunction(
                        FunSpec.builder("onEvent")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter(
                                ParameterSpec.builder(
                                    "event",
                                    ClassName(
                                        "io.github.ydwk.ydwk.evm.backend.event",
                                        "Event"
                                    )
                                ).build()
                            )
                            .addStatement(
                                "when(event) {" +
                                        channelListeners.forEach {
                                            "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                        }.joinToString("\n") +
                                        "}"
                            )
                            .build()
                    )
                    ).build()

            val userListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "UserListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("UserListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                userListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            ).addFunction(
                                FunSpec.builder("onEvent")
                                    .addModifiers(KModifier.OVERRIDE)
                                    .addParameter(
                                        ParameterSpec.builder(
                                            "event",
                                            ClassName(
                                                "io.github.ydwk.ydwk.evm.backend.event",
                                                "Event"
                                            )
                                        ).build()
                                    )
                                    .addStatement(
                                        "when(event) {" +
                                                userListeners.forEach {
                                                    "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                                } .joinToString("\n") + 
                                                "}"
                                    )
                                    .build()
                            )
                    ).build()

            val voiceListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "VoiceListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("VoiceListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                voiceListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            )
                    .addFunction(
                        FunSpec.builder("onEvent")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter(
                                ParameterSpec.builder(
                                    "event",
                                    ClassName(
                                        "io.github.ydwk.ydwk.evm.backend.event",
                                        "Event"
                                    )
                                ).build()
                            )
                            .addStatement(
                                "when(event) {" +
                                        voiceListeners.forEach {
                                            "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                        }.joinToString("\n") +
                                        "}"
                            )
                            .build()
                    )
                    ).build()

            val interactionListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "InteractionListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("InteractionListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                interactionListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            )
                    .addFunction(
                        FunSpec.builder("onEvent")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter(
                                ParameterSpec.builder(
                                    "event",
                                    ClassName(
                                        "io.github.ydwk.ydwk.evm.backend.event",
                                        "Event"
                                    )
                                ).build()
                            )
                            .addStatement(
                                "when(event) {" +
                                        interactionListeners.forEach {
                                            "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                        }.joinToString("\n") +
                                        "}"
                            )
                            .build()
                    )
                    ).build()


            val guildModerationListenerFile: FileSpec.Builder =
                FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "GuildModerationListeners")
                    .addType(
                        TypeSpec.interfaceBuilder("GuildModerationListeners")
                            .addSuperinterface(
                                ClassName(
                                    "io.github.ydwk.ydwk.evm.backend.event.IEventListener",
                                    "IEventListener"))
                            .addFunction(
                                guildModerationListeners.forEach {
                                    FunSpec.builder("0n" + it.simpleName)
                                        .addModifiers(KModifier.ABSTRACT)
                                        .addParameter(
                                            ParameterSpec.builder(
                                                "event",
                                                ClassName(
                                                    it.packageName,
                                                    it.simpleName
                                                )
                                            ).build()
                                        )
                                        .build()
                                }
                            )
                    .addFunction(
                        FunSpec.builder("onEvent")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter(
                                ParameterSpec.builder(
                                    "event",
                                    ClassName(
                                        "io.github.ydwk.ydwk.evm.backend.event",
                                        "Event"
                                    )
                                ).build()
                            )
                            .addStatement(
                                "when(event) {" +
                                        guildModerationListeners.forEach {
                                            "is ${it.packageName}.${it.simpleName} -> 0n${it.simpleName}(event)"
                                        }.joinToString("\n") +
                                        "}"
                            )
                            .build()
                    )
                    ).build()
      
            val eventFile = FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", "Events")
                            //add the above inline function
                            .addFunction(FunSpec.builder("on")
                                .addModifiers(KModifier.INLINE)
                                .addTypeVariable(TypeVariableName("reified T : GenericEvent"))
                                .addParameter("consumer", LambdaTypeName.get(
                                    parameters = listOf(
                                        ParameterSpec.builder("event", ClassName("io.github.ydwk.ydwk.evm.backend.event", "GenericEvent")).build(),
                                        ParameterSpec.builder("T", ClassName("io.github.ydwk.ydwk.evm.backend.event", "GenericEvent")).build()
                                    ),
                                    returnType = Unit::class.asClassName()
                                ))
                                .returns(ClassName("io.github.ydwk.ydwk.evm.backend.event", "CoroutineEventListener"))
                                .addStatement("return object : CoroutineEventListener {\n" +
                                        "            override suspend fun onEvent(event: GenericEvent) {\n" +
                                        "                if (event is T) {\n" +
                                        "                    event.consumer(event)\n" +
                                        "                }\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "        .also { this.addEventListeners(it) }")
                                .build())
                                //now add one each for the event classes returned by the above functions
                                

            logger.lifecycle("Writing files")
            File("${project.buildDir}/generated/kotlin").apply {
                try {
                    mkdirs()
                    coreListenerFile.build().writeTo(this)
                    guildListenerFile.build().writeTo(this)
                    channelListenerFile.build().writeTo(this)
                    userListenerFile.build().writeTo(this)
                    voiceListenerFile.build().writeTo(this)
                    interactionListenerFile.build().writeTo(this)
                    guildModerationListenerFile.build().writeTo(this)

                    eventFile.build().writeTo(this)

                    logger.lifecycle("Done")
                } catch (e: Exception) {
                    e.printStackTrace()
                    logger.error("Failed to write files")
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        logger.error("Failed to generate files")
    }
}
