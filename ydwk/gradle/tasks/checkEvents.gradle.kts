/** Check if event classes are data classes unless they are open classes. */
tasks.register("checkEvents") { doLast { checkForDataClasses() } }

fun checkForDataClasses() {
    if (File("src/main/kotlin/io/github/ydwk/ydwk/evm/backend/event/GenericEvent.kt").exists()) {
        val files = File("src/main/kotlin/io/github/ydwk/ydwk/evm/event/events").listFiles()
        // converse these files to kotlin classes

        if (files.isNullOrEmpty()) {
            throw Exception("No files found in the events folder")
        } else {
            for (file in files) {
                val folder =
                    File("src/main/kotlin/io/github/ydwk/ydwk/evm/event/events/" + file.name)
                if (folder.exists()) {
                    if (!folder.listFiles().isNullOrEmpty()) {
                        checkForDataClassesProcess(folder)
                    }
                } else {
                    throw Exception("Folder $folder does not exist")
                }
            }
        }
    }
}

fun checkForDataClassesProcess(folder: File) {
    for (subFiles in folder.listFiles()!!) {

        if (subFiles.isDirectory) {
            val subFilesDir = File(subFiles.path)
            if (subFilesDir.exists()) {
                if (!subFilesDir.listFiles().isNullOrEmpty()) {
                    checkForDataClassesProcess(subFilesDir)
                }
            } else {
                throw Exception("Folder $subFilesDir does not exist")
            }
        } else {
            val classFile: File = subFiles!!
            val className = classFile.nameWithoutExtension
            // need to take a different approach
            // since i cant get the class from the file name, I will use the raw data from the file
            // and look to see if before class it has data
            // if it does, then it is a data class
            // if it doesn't, then it is not a data class and throw an exception

            val dataClass = classFile.readText().contains("data class")
            val openClass = classFile.readText().contains("open class")

            if (!dataClass && !openClass) {
                throw Exception(
                    "The class $className is not a data class. Please make it a data class")
            }
        }
    }
}
