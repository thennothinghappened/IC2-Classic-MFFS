
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.minecraftforge.net/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("net.minecraftforge.gradle") version "6.0.+" apply false
    kotlin("jvm") version "2.0.0" apply false
}
