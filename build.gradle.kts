import net.minecraftforge.gradle.userdev.UserDevExtension
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

version = "1.0.0"
group = "orca.mods.mffs"

plugins {
    id("net.minecraftforge.gradle")
    kotlin("jvm")
}

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

    minecraft("net.minecraftforge:forge:1.12.2-14.23.5.2859")

    // https://stackoverflow.com/questions/68377027/minecraft-forge-mod-loader-fml-loading-and-crashing-mc
    implementation("net.minecraftforge:mergetool:0.2.3.3")
    implementation("curse.maven:ic2_classic-242942:4476676")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

configure<UserDevExtension> {

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

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")
        }
    }
}

tasks.withType<Jar> {

    archiveBaseName.set("ic2c-mffs_1.12.2-$version")

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

sourceSets.all {
    output.setResourcesDir(output.classesDirs.files.iterator().next())
}
