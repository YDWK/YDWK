import com.squareup.kotlinpoet.*
import java.util.*

buildscript {
    repositories { mavenCentral() }

    dependencies {
        classpath("com.squareup:kotlinpoet:" + properties["kotlinPoetVersion"])
        classpath("io.github.classgraph:classgraph:4.8.157")
    }
}
