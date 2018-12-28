import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.jetbrains.intellij") version "0.4.1"
}

repositories {
    mavenCentral()
}

val junitVersion: String by properties
val jmockitVersion: String by properties
val assertjVersion: String by properties
val mockitoVersion: String by properties
val spekVersion: String by properties
val junitJupiterApiVersion: String by properties
val junitJupiterEngineVersion: String by properties

dependencies {
  compile(project(":aem-intellij-core"))
  compile(project(":aem-intellij-common"))

  compile("junit:junit:$junitVersion")
  compile("org.jmockit:jmockit:$jmockitVersion")
  compile("org.assertj:assertj-core:$assertjVersion")
  compile("org.mockito:mockito-core:$mockitoVersion")
  compile("org.jetbrains.spek:spek-api:$spekVersion")

  compile("org.junit.jupiter:junit-jupiter-api:$junitJupiterApiVersion")
  compile("org.junit.jupiter:junit-jupiter-engine:$junitJupiterEngineVersion")
}

val ideaVersion: String by properties

intellij {
  version = ideaVersion
  setPlugins("IntelliLang")
}
