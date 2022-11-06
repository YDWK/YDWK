import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.printer.PrettyPrinter
import com.github.javaparser.printer.PrettyPrinterConfiguration
import io.github.classgraph.ClassGraph
import java.io.FileWriter

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("io.github.classgraph:classgraph:4.8.149")
        classpath("com.github.javaparser:javaparser-core:3.24.7")
    }
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
    val buildDir = project.buildDir.toString() + "/generated/events"

    // create the folder if it doesn't exist
    val buildDirFile = File(buildDir)
    if (buildDirFile.exists()) {
        buildDirFile.delete()
        try {
            buildDirFile.mkdirs()
        } catch (e: Exception) {
            throw RuntimeException("Error creating build directory $e")
        }
    } else {
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

    val newUpdateEventFile = CompilationUnit("io.github.ydwk.ydwk.entities.events.${className}")

    newUpdateEventFile.addImport("io.github.ydwk.ydwk.entities.${className}")
    newUpdateEventFile.addImport("import io.github.ydwk.ydwk.evm.backend.event.IEventUpdate")
    val newUpdateEventClass =
        newUpdateEventFile
            .addClass(className + "UpdateEvent")
            .addExtends("IEventUpdate<${className}>")
}

fun createEventFile(buildDir: File) {
    val baseEventFile = CompilationUnit("io.github.ydwk.ydwk.events.Event.java")

    try {
        logger.lifecycle("Creating Event.kt...")
        baseEventFile.addImport("io.github.ydwk.ydwk.YDWK")
        baseEventFile.addImport("io.github.ydwk.ydwk.evm.backend.event.IEvent")

        val baseEventClass = baseEventFile.addClass("Event").addExtends("IEvent")

        baseEventFile.storage.ifPresent {
            it.save {
                logger.lifecycle("Saving Event.kt...")
                PrettyPrinter(
                        PrettyPrinterConfiguration()
                            .setOrderImports(true)
                            .setEndOfLineCharacter("\n")
                            .setColumnAlignParameters(true)
                            .setColumnAlignFirstMethodChain(true))
                    .print(it)
            }
        }
            ?: throw RuntimeException("Error saving Event.kt")
    } catch (e: Exception) {
        throw RuntimeException("Error creating base event class $e")
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
