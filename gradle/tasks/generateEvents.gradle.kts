import io.github.classgraph.ClassGraph
import java.io.FileWriter

buildscript {
    repositories { mavenCentral() }

    dependencies { classpath("io.github.classgraph:classgraph:4.8.149") }
}

apply(plugin = "java")

val mainSrc = project(":")

tasks.create("generateEvents") {
    doLast {
        // search every interface that extends GenericEntity and look for variables with the
        // annotation @UpdatableVariable
        val allVariablesContaingUpdatableVariableAnotation:
            MutableList<io.github.classgraph.ClassInfo> =
            mutableListOf()

        val rawRootDir: String =
            mainSrc.projectDir.toString() + "/src/main/kotlin/io/github/ydwk/ydwk"
        val parsedRootDir: String = parseRootDir(rawRootDir)

        try {

            ClassGraph().acceptPackages(parsedRootDir).enableAllInfo().scan().use { scanResult ->
                val allGenericEntities =
                    scanResult.getClassesImplementing(
                        "io.github.ydwk.ydwk.entities.util.GenericEntity")
                allGenericEntities.forEach { genericEntity ->
                    val allFields = genericEntity.fieldAnnotations.annotations

                    allFields.forEach { field ->
                        if (field.name ==
                            "io.github.ydwk.ydwk.entities.util.annotations.UpdatableVariable") {
                            allVariablesContaingUpdatableVariableAnotation.add(genericEntity)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error getting all classes with UpdatableVariable annotation $e")
        }

        try {
            generateEvents(allVariablesContaingUpdatableVariableAnotation)
        } catch (e: Exception) {
            throw RuntimeException("Error generating events $e")
        }
    }
}

fun parseRootDir(rawRootDir: String): String {
    return rawRootDir.replace("\\", ".")
}

fun generateEvents(annotations: List<io.github.classgraph.ClassInfo>) {
    logger.lifecycle("Generating events...")

    // generate in the build folder
    val buildDir =
        project.buildDir.toString() + "/src/main/kotlin/io/github/ydwk/ydwk/generated/events"

    // create the folder if it doesn't exist
    val buildDirFile = File(buildDir)
    if (!buildDirFile.exists()) {
        try {
            buildDirFile.mkdirs()
        } catch (e: Exception) {
            throw RuntimeException("Error creating build directory $e")
        }
    }

    try {
        createEventFile(buildDirFile)
    } catch (e: Exception) {
        throw RuntimeException("Error creating events file " + e.message)
    }

    try {
        logger.lifecycle("Creating event classes...")
        createEventClass(annotations, buildDirFile)
    } catch (e: Exception) {
        throw RuntimeException("Error creating event classes")
    }
}

fun createNewUpdateEvent(className: String, classDir: File) {
    logger.lifecycle("Creating ${className}UpdateEvent.kt...")

    val newUpdateEventFile = File(classDir, "${className}UpdateEvent.kt")

    if (!newUpdateEventFile.exists()) {
        newUpdateEventFile.createNewFile()

        val newUpdateEventFileWriter = FileWriter(newUpdateEventFile)

        newUpdateEventFileWriter.write("package io.github.ydwk.ydwk.events.${className}")
        newUpdateEventFileWriter.write("\n")
        newUpdateEventFileWriter.write("import io.github.ydwk.ydwk.YDWK")
        newUpdateEventFileWriter.write("\n")
        newUpdateEventFileWriter.write("import io.github.ydwk.ydwk.entities.${className}")
        newUpdateEventFileWriter.write("import io.github.ydwk.ydwk.evm.backend.event.IEventUpdate")
        newUpdateEventFileWriter.write("\n")
        newUpdateEventFileWriter.write(
            """
            open class ${className}UpdateEvent(
                override val ydwk: YDWK,
                override val entity: ${className},
                override val oldValue: ${className}.${className}Variables,
                override val newValue: ${className}.${className}Variables
            ) : IEventUpdate<${className}, ${className}.${className}Variables>
        """.trimIndent())

        newUpdateEventFileWriter.close()
        logger.info("Created new update event for $className")
    }
}

fun createEventFile(buildDir: File) {
    logger.lifecycle("Creating Event.kt...")

    val baseEventFile = File(buildDir, "Event.kt")

    if (!baseEventFile.exists()) {
        baseEventFile.createNewFile()

        val baseEventFileWriter = FileWriter(baseEventFile)

        baseEventFileWriter.write("package io.github.ydwk.ydwk.events")
        baseEventFileWriter.write("\n")
        baseEventFileWriter.write("import io.github.ydwk.ydwk.YDWK")
        baseEventFileWriter.write("import io.github.ydwk.ydwk.evm.backend.event.IEvent")
        baseEventFileWriter.write("\n")
        baseEventFileWriter.write(
            """
          open class Event(override val ydwk: YDWK) : GenericEvent
        """.trimIndent())

        baseEventFileWriter.close()
        logger.lifecycle("Created Event.kt")
    }
}

fun createEventClass(annotations: List<io.github.classgraph.ClassInfo>, eventsDir: File) {
    annotations.forEach { genericEntity ->
        // get the class name
        val className = genericEntity.simpleName

        // if a folder does not exist for that class name, create it
        val classDir = File(eventsDir, className)

        try {
            if (!classDir.exists()) {
                classDir.mkdir()
            }
        } catch (e: Exception) {
            throw RuntimeException("Error creating class directory")
        }

        // create a Generic + className + UpdateEvent.kt file with a generic T
        val genericUpdateEventFile = File(classDir, "Generic${className}UpdateEvent.kt")

        if (!genericUpdateEventFile.exists()) {
            genericUpdateEventFile.createNewFile()

            val genericUpdateEventFileWriter = FileWriter(genericUpdateEventFile)

            genericUpdateEventFileWriter.write("package io.github.ydwk.ydwk.events.${className}")
            genericUpdateEventFileWriter.write("\n")
            genericUpdateEventFileWriter.write("import io.github.ydwk.ydwk.YDWK")
            genericUpdateEventFileWriter.write("import io.github.ydwk.ydwk.entities.${className}")
            genericUpdateEventFileWriter.write(
                "import io.github.ydwk.ydwk.evm.backend.event.IEventUpdate")
            genericUpdateEventFileWriter.write("\n")
            genericUpdateEventFileWriter.write(
                """
                    open class Generic${className}UpdateEvent<T>(
                        override val ydwk: YDWK,
                        override val entity: ${className},
                        override val oldValue: T,
                        override val newValue: T
                    ) : IEventUpdate<${className}, T>
                """.trimIndent())
        }

        logger.lifecycle("Created Generic${className}UpdateEvent.kt")
        // create new event file
        createNewUpdateEvent(className, classDir)
    }
}

class EventFileWriter(private val eventFile: File) {
    private val fileWriter: FileWriter = FileWriter(eventFile)

    fun write(s: String) {
        fileWriter.write(s)
    }

    fun close() {
        fileWriter.close()
    }
}
