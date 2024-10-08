import com.squareup.kotlinpoet.*
import java.io.File

buildscript {
    repositories { mavenCentral() }

    dependencies { classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"]) }
}

fun addFunction(): FunSpec.Builder {
    return FunSpec.builder("on")
        .addKdoc(
            """
                    Listens for events of type [T].

                    @param consumer The consumer function to handle the event.
                    @return The [T] event listener.
                """
                .trimIndent())
        .addModifiers(KModifier.INLINE)
        .addTypeVariable(TypeVariableName("reified T : GenericEvent"))
        .addParameter(
            "consumer",
            LambdaTypeName.get(
                    receiver = ClassName("io.github.ydwk.ydwk.evm.event", "GenericEvent"),
                    parameters = listOf(ParameterSpec.builder("", TypeVariableName("T")).build()),
                    returnType = UNIT)
                .copy(suspending = true),
            KModifier.CROSSINLINE)
        .returns(ClassName("io.github.ydwk.ydwk.evm.event", "CoroutineEventListener"))
        .addCode(
            """
            return object : CoroutineEventListener {
                override suspend fun onEvent(event: GenericEvent) {
                    if (event is T) {
                        event.consumer(event)
                    }
                }
            }
            """
                .trimIndent())
        .addCode(".also { ydwk.addEventListeners(it) }")
}

fun addEventSpecification(name: String): FunSpec {
    return FunSpec.builder("on${name}")
        .addKdoc(
            """
            Listens for [${name}].

            @param consumer The consumer function to handle the ${name}.
            @return The [${name}] listener.
        """
                .trimIndent())
        .addParameter(
            "consumer",
            LambdaTypeName.get(
                    receiver = ClassName("io.github.ydwk.ydwk.evm.event", "GenericEvent"),
                    parameters = listOf(ParameterSpec.builder("", TypeVariableName(name)).build()),
                    returnType = UNIT)
                .copy(suspending = true),
            KModifier.CROSSINLINE)
        .addModifiers(KModifier.PUBLIC, KModifier.INLINE)
        .returns(ClassName("io.github.ydwk.ydwk.evm.event", "CoroutineEventListener"))
        .addCode(
            """
                           return on<${name}>(consumer)
                        """
                .trimIndent())
        .build()
}

fun addEventSpecificationInflux(name: String): FunSpec {
    return FunSpec.builder("on${name}")
        .addKdoc(
            """
            Listens for [${name}].

            @param consumer The consumer function to handle the ${name}.
            @return The [${name}] listener.
        """
                .trimIndent())
        .addParameter(
            "consumer",
            LambdaTypeName.get(
                    receiver = ClassName("io.github.ydwk.ydwk.evm.event", "GenericEvent"),
                    parameters = listOf(ParameterSpec.builder("", TypeVariableName(name)).build()),
                    returnType = UNIT)
                .copy(suspending = true),
            KModifier.CROSSINLINE)
        .addModifiers(KModifier.PUBLIC, KModifier.INLINE, KModifier.INFIX)
        .receiver(ClassName("io.github.ydwk.ydwk", "YDWK")) // Add receiver parameter
        .returns(ClassName("io.github.ydwk.ydwk.evm.event", "CoroutineEventListener"))
        .addCode(
            """
            return this.eventListener.on<${name}>(consumer)
        """
                .trimIndent())
        .build()
}

fun generateEventFile() {
    val eventsFolder = File(projectDir, "src/main/kotlin/io/github/ydwk/ydwk/evm/event/events")

    val channelEvents = mutableMapOf<String, String>()
    val gatewayEvents = mutableMapOf<String, String>()
    val guildEvents = mutableMapOf<String, String>()
    val guildModerationEvents = mutableMapOf<String, String>()
    val interactionEvents = mutableMapOf<String, String>()
    val userEvents = mutableMapOf<String, String>()
    val voiceEvents = mutableMapOf<String, String>()

    eventsFolder.walk().forEach {
        if (it.isFile && it.name.endsWith(".kt")) {
            val file = it.readText()
            val name = it.name.substring(0, it.name.length - 3)
            val sanitizedPath = it.absolutePath.replace('\\', '/')

            when {
                file.contains("@ChannelEvent") -> {
                    channelEvents[name] = sanitizedPath
                }
                file.contains("@GatewayEvent") -> {
                    gatewayEvents[name] = sanitizedPath
                }
                file.contains("@GuildEvent") -> {
                    guildEvents[name] = sanitizedPath
                }
                file.contains("@GuildModerationEvent") -> {
                    guildModerationEvents[name] = sanitizedPath
                }
                file.contains("@InteractionEvent") -> {
                    interactionEvents[name] = sanitizedPath
                }
                file.contains("@UserEvent") -> {
                    userEvents[name] = sanitizedPath
                }
                file.contains("@VoiceEvent") -> {
                    voiceEvents[name] = sanitizedPath
                }
            }
        }
    }

    val eventClass =
        TypeSpec.classBuilder("EventListeners")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("ydwk", ClassName("io.github.ydwk.ydwk", "YDWK"))
                    .build())
            .addProperties(
                listOf(
                    PropertySpec.builder("ydwk", ClassName("io.github.ydwk.ydwk", "YDWK"))
                        .initializer("ydwk")
                        .build()))
            .addModifiers(KModifier.PUBLIC)
            .addFunction(addFunction().build())

    for ((name, _) in channelEvents) {
        // do public inline fun on{name}Event { return  on<name>Event> }

        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in gatewayEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in guildEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in guildModerationEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in interactionEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in userEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    for ((name, _) in voiceEvents) {
        eventClass.addFunction(addEventSpecification(name))
    }

    // companion object

    val eventClassCompanionObject = TypeSpec.companionObjectBuilder()

    for ((name, _) in channelEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in gatewayEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in guildEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in guildModerationEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in interactionEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in userEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    for ((name, _) in voiceEvents) {
        eventClassCompanionObject.addFunction(addEventSpecificationInflux(name))
    }

    eventClass.addType(eventClassCompanionObject.build())

    val eventFile =
        FileSpec.builder("io.github.ydwk.ydwk.evm.event", "EventListeners")
            .addImport("io.github.ydwk.ydwk", "YDWK")
            .addImport("io.github.ydwk.ydwk.evm.backend.event", "GenericEvent")
            .addImport("io.github.ydwk.ydwk.evm.backend.event", "CoroutineEventListener")
            .addType(eventClass.build())

    for ((name, path) in channelEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in gatewayEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in guildEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in guildModerationEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in interactionEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in userEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    for ((name, path) in voiceEvents) {
        eventFile.addImport(
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", "."),
            name)
    }

    File("${project.layout.buildDirectory.get().getAsFile()}/generated/kotlin").apply {
        mkdirs()

        eventFile.build().writeTo(this)
    }

    generateEventListenersInterfaces("ChannelEventListener", channelEvents)
    generateEventListenersInterfaces("GatewayEventListener", gatewayEvents)
    generateEventListenersInterfaces("GuildEventListener", guildEvents)
    generateEventListenersInterfaces("GuildModerationEventListener", guildModerationEvents)
    generateEventListenersInterfaces("InteractionEventListener", interactionEvents)
    generateEventListenersInterfaces("UserEventListener", userEvents)
    generateEventListenersInterfaces("VoiceEventListener", voiceEvents)
}

fun generateEventListenersInterfaces(listenerName: String, listenerEvents: Map<String, String>) {
    val interfaceClass =
        TypeSpec.interfaceBuilder(listenerName)
            .addModifiers(KModifier.PUBLIC)
            .addSuperinterface(ClassName("io.github.ydwk.ydwk.evm.backend.event", "IEventListener"))

    val onEventFunction =
        FunSpec.builder("onEvent")
            .addModifiers(
                KModifier.PUBLIC,
                KModifier.OVERRIDE,
                KModifier.SUSPEND) // Add KModifier.SUSPEND here
            .addParameter(
                "event", ClassName("io.github.ydwk.ydwk.evm.backend.event", "GenericEvent"))
            .beginControlFlow("when (event)")

    for ((name, path) in listenerEvents) {
        val eventPackage =
            path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", ".")
        val eventClassName = ClassName(eventPackage, name)

        interfaceClass.addFunction(
            FunSpec.builder("on$name")
                .addKdoc("Listens to the $name event.\n\n@param event The $name event.")
                .addModifiers(KModifier.PUBLIC, KModifier.SUSPEND)
                .addParameter("event", eventClassName)
                .build())

        onEventFunction.addStatement("is %T -> on$name(event)", eventClassName)
    }

    onEventFunction.endControlFlow()

    interfaceClass.addFunction(onEventFunction.build())

    val interfaceFile =
        FileSpec.builder("io.github.ydwk.ydwk.evm.listeners", listenerName)
            .addImport("io.github.ydwk.ydwk.evm.backend.event", "IEventListener")
            .addType(interfaceClass.build())
            .build()

    File("${project.layout.buildDirectory.get().getAsFile()}/generated/kotlin").apply {
        mkdirs()
        interfaceFile.writeTo(this)
    }
}

tasks.create("generateEvents") {
    group = "generate"

    doLast { generateEventFile() }
}
