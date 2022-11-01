/**
 * check if entities interfaces from the folder src/main/kotlin/io/github/ydwk/ydwk/entities extend
 * GenericEntity
 */
tasks.register("checkEntities") {
    doLast {
        val entities = File("src/main/kotlin/io/github/ydwk/ydwk/entities")
        val entitiesInterfaces = entities.listFiles { file -> file.extension == "kt" }
        entitiesInterfaces?.forEach { file ->
            val lines = file.readLines()
            val isGenericEntity = lines.any { line -> line.contains("GenericEntity") }
            if (!isGenericEntity) {
                throw GradleException("Entity ${file.name} does not extend GenericEntity")
            }
        }
    }
}
