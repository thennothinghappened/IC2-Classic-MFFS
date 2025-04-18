import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.tasks.JarJar
import proguard.gradle.ProGuardTask
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

version = "0.1.0"
group = "mods.orca.mffs"

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.5.+")
    }
}

plugins {
    id("net.minecraftforge.gradle")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

/**
 * Configuration for libraries to be included into the fat jar.
 * We use this for the Kotlin Standard Library as it is not available in Minecraft's classpath.
 */
val shadow: Configuration by configurations.creating {
    exclude("org.jetbrains", "annotations")
}

// Enable creating a fat jar.
jarJar.enable()

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://cursemaven.com")
        }
        filter {
            includeGroup("curse.maven")
        }
    }
}

dependencies {

    shadow("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
    shadow("org.jetbrains.kotlin:kotlin-stdlib-common:${kotlin.coreLibrariesVersion}")

    minecraft("net.minecraftforge:forge:1.12.2-14.23.5.2860")
    implementation("curse.maven:ic2_classic-242942:5167044")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.1")

}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
        freeCompilerArgs.add("-Xcontext-receivers")
    }

    jvmToolchain(8)

}

minecraft {

    mappings("stable",  "39-1.12")

    runs {

        create("client") {

            workingDirectory(project.file("run"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

        }

        create("server") {

            workingDirectory(project.file("run/server"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

        }

    }
}

tasks {

    processResources {
        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to "1.12.2"))
        }
    }

    val outputFileName = "ic2c-mffs_1.12.2"

    withType<Jar> {

        archiveBaseName.set(outputFileName)

        manifest {
            attributes(
                "Specification-Title" to "mffs",
                "Specification-Vendor" to "orca",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to version,
                "Implementation-Vendor" to "orca",
                "Implementation-Timestamp" to LocalDateTime.now()
                    .atOffset(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))
            )
        }

    }

    /**
     * Include shadow dependencies in fat jar.
     */
    jarJar.configure {
        from(provider { shadow.map(::zipTree).toTypedArray() })
    }

    /**
     * Task that runs ProGuard to minify (not obfuscate!) the fat jar. We use this since we're including the
     * Kotlin stdlib, which otherwise increases size significantly.
     */
    val proguardReleaseJarJar = register<ProGuardTask>("proguardReleaseJarJar") {

        dependsOn("jarJar")

        configuration("src/proguard-rules.pro")

        named<JarJar>("jarJar").let { jarTask ->

            injars(jarTask.flatMap { it.archiveFile })
            outjars(jarTask.flatMap {
                layout.buildDirectory.file("libs/${it.archiveFile.get().asFile.nameWithoutExtension}-minified.jar")
            })

        }

        // Inform Proguard of the libraries we have!
        libraryjars("${System.getProperty("java.home")}/lib/rt.jar")
        libraryjars(configurations.runtimeClasspath)

    }

    /**
     * Task that produces the release fat Jar output, reobfuscated and minified.
     */
    register<JarJar>("releaseJarJar") {

        group = "build"
        archiveBaseName.set("$outputFileName-release")

        dependsOn(proguardReleaseJarJar)

        from(proguardReleaseJarJar.map { zipTree(it.outputs.files.asPath) })
        configuration(project.configurations.jarJar.get())

    }

}

sourceSets.all {
    output.setResourcesDir(output.classesDirs.files.iterator().next())
}
