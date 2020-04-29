import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.Options
import net.minecraftforge.gradle.common.util.MinecraftExtension

val minecraftVersion: String by project
val curseProjectId: String by project
val curseMinecraftVersion: String by project
val modJarBaseName: String by project
val modMavenGroup: String by project
val modPlatform: String by project

buildscript {
    repositories {
        maven(url = "https://files.minecraftforge.net/maven")
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(group="net.minecraftforge.gradle", name="ForgeGradle", version="3.+")
                .setChanging(true)
                .exclude(group="trove", module = "trove")
    }
}

plugins {
    java
    idea
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

apply(plugin="net.minecraftforge.gradle")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

base {
    archivesBaseName = "$modJarBaseName-mc$minecraftVersion-$modPlatform"
}

repositories {
    maven(url = "https://files.minecraftforge.net/maven")
    mavenCentral()
    jcenter()
    maven(url= "https://minecraft.curseforge.com/api/maven/")
}

version = "2.1.0"
group = modMavenGroup

configure<MinecraftExtension> {
    mappings("snapshot", "20200424-1.15.1")

    runs {
        create("client")
        create("server")
        create("data") {
            args("--mod", "iamverysmart", "--all", "--output", file("src/generated/resources"))
        }

        configureEach {
            workingDirectory(project.file("run/$name"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")

            mods {
                create("iamverysmart") {
                    source(sourceSets["main"])
                }
            }
        }
    }
}

dependencies {
    "minecraft"("net.minecraftforge:forge:$minecraftVersion-31.1.47")
}

val jar: Jar by tasks
jar.apply {
    manifest {
        attributes(
                mapOf(
                        "Specification-Title" to "iamverysmart",
                        "Specification-Vendor" to "sargunv",
                        "Specification-Version" to "1",
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to "${project.version}",
                        "Implementation-Vendor" to "sargunv"
                )
        )
    }
}

curseforge {
    if (project.hasProperty("curseforge_api_key")) {
        apiKey = project.property("curseforge_api_key")!!
    }

    project(closureOf<CurseProject> {
        id = curseProjectId
        releaseType = "release"
        addGameVersion(curseMinecraftVersion)
        addGameVersion(modPlatform.capitalize())
    })

    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
