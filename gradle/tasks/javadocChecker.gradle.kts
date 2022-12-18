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
                // look for fun and val/var and check if above it has javadoc if not throw error. If
                // the
                // javadoc starts with "Used" or "used" throw error.

                val lines = file.readLines()

                val isFunMethod = lines.any { line -> line.contains("fun") }

                if (isFunMethod) {
                    val isFunMethodJavadoc = lines.any { line -> line.contains("/**") }
                    if (!isFunMethodJavadoc) {
                        throw GradleException("Event interface ${file.name} does not have javadoc")
                    }

                    val isFunMethodJavadocUsed = lines.any { line -> line.contains("* Used") }

                    if (isFunMethodJavadocUsed) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with Used")
                    }

                    val isFunMethodJavadocUsedLowerCase =
                        lines.any { line -> line.contains("* used") }

                    if (isFunMethodJavadocUsedLowerCase) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with used")
                    }
                }

                val isVarMethod = lines.any { line -> line.contains("var") }

                if (isVarMethod) {
                    val isValMethodJavadoc = lines.any { line -> line.contains("/**") }
                    if (!isValMethodJavadoc) {
                        // try to get line number of the var
                        throw GradleException("Event interface ${file.name} does not have javadoc")
                    }

                    val isValMethodJavadocUsed = lines.any { line -> line.contains("* Used") }

                    if (isValMethodJavadocUsed) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with Used")
                    }

                    val isValMethodJavadocUsedLowerCase =
                        lines.any { line -> line.contains("* used") }

                    if (isValMethodJavadocUsedLowerCase) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with used")
                    }
                }

                val isValMethod = lines.any { line -> line.contains("val") }

                if (isValMethod) {
                    val isValMethodJavadoc = lines.any { line -> line.contains("/**") }
                    if (!isValMethodJavadoc) {
                        throw GradleException("Event interface ${file.name} does not have javadoc")
                    }

                    val isValMethodJavadocUsed = lines.any { line -> line.contains("* Used") }

                    if (isValMethodJavadocUsed) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with Used")
                    }

                    val isValMethodJavadocUsedLowerCase =
                        lines.any { line -> line.contains("* used") }

                    if (isValMethodJavadocUsedLowerCase) {
                        throw GradleException(
                            "Event interface ${file.name} javadoc starts with used")
                    }
                }
            }
        }
    }
}
