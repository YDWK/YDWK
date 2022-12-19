tasks.register("javadocChecker") {
    doLast {
        val foldersToCheck =
            listOf(
                File("src/main/kotlin/io/github/ydwk/ydwk/entities"),
                File("src/main/kotlin/io/github/ydwk/ydwk/evm/event/events"))
        val entitiesInterfacesOrDataClasses =
            foldersToCheck.flatMap {
                it.walk().filter { it.isFile && it.extension == "kt" }.toList()
            }
        entitiesInterfacesOrDataClasses.forEach { file ->
            // Only look for functions and variables with entites but with events check the top of
            // the data class name. Check if the line starts with "fun" or "val" and check if above
            // it has javadoc if not throw error. If the javadoc starts with "Used" or "used" throw
            // error.

            val lines = file.readLines()

            val isDataClass = lines.any { line -> line.contains("data class") }
            if (isDataClass) {
                val isDataClassJavadoc = lines.any { line -> line.contains("/**") }
                if (!isDataClassJavadoc) {
                    throw GradleException("Data class ${file.name} does not have javadoc")
                }

                val isDataClassJavadocUsed = lines.any { line -> line.contains("* Used") }

                if (isDataClassJavadocUsed) {
                    throw GradleException("Data class ${file.name} javadoc starts with Used")
                }

                val isDataClassJavadocUsedLowerCase = lines.any { line -> line.contains("* used") }

                if (isDataClassJavadocUsedLowerCase) {
                    throw GradleException("Data class ${file.name} javadoc starts with used")
                }
            }

            val eventInterfaces = lines.any { line -> line.contains("interface") }
            if (eventInterfaces) {
                // we are not checking if the entity name has javadoc above.
                // e.g interface Bot : User, GenericEntity
                // we are not checking if the interface has javadoc above.
                // but we are checking if var email: String has javadoc above.
                val lines = file.readLines()

                for (i in lines.indices) {
                    val line = lines[i]
                    // keep going until you pas the interface line
                    val fileNameFromInterfaceToBracket =
                        line.substringAfter("interface").substringBefore("{")
                    if (line.contains(fileNameFromInterfaceToBracket)) {
                        // ignore and continue
                        continue
                    }

                    // this is an example interface
                    // interface WelcomeChannel : GenericEntity {
                    //    /**
                    //     * The channel's id
                    //     *
                    //     * @return The channel's id
                    //     */
                    //    val channelId: GetterSnowFlake

                    // we ignore interface WelcomeChannel : GenericEntity
                    // and we start checking from the next line
                    // if we hit a line that starts with "fun" or "val" we check if the line above
                    // and see if it has */ meaining it has javadoc
                    // we also need to check if the javadoc starts with "Used" or "used" which is
                    // done through by keeping going up
                    // until we hit /** and then move to the next line and check if it starts with
                    // "Used" or "used"
                    // if it does throw error

                    println("line: $line")

                    if (line.startsWith("fun") ||
                        line.startsWith("val") ||
                        line.startsWith("var")) {
                        val lineAbove = lines[i - 1]
                        if (!lineAbove.contains("*/")) {
                            // check if it is variable or function and throw error accordingly
                            if (line.startsWith("fun")) {
                                throw GradleException(
                                    "Function ${line} does not have javadoc in ${file.name}")
                            } else {
                                throw GradleException(
                                    "Variable ${line} does not have javadoc in ${file.name}")
                            }
                        }

                        val lineAboveJavadoc = // we dont know how many lines the javadoc is so we
                        // keep going up until we hit /**
                        lines
                                .subList(0, i - 1)
                                .reversed()
                                .takeWhile { line -> !line.contains("/**") }
                                .reversed()

                        val isJavadocUsed = lineAboveJavadoc.any { line -> line.contains("* Used") }
                        if (isJavadocUsed) {
                            throw GradleException(
                                "Javadoc for ${line} in ${file.name} starts with Used")
                        }

                        val isJavadocUsedLowerCase =
                            lineAboveJavadoc.any { line -> line.contains("* used") }
                        if (isJavadocUsedLowerCase) {
                            throw GradleException(
                                "Javadoc for ${line} in ${file.name} starts with used")
                        }
                    }
                }
            }
        }
    }
}
