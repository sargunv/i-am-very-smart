import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask

plugins {
    java
    idea
    `maven-publish`
    id("fabric-loom") version "0.4-SNAPSHOT"
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

base {
    archivesBaseName = "i-am-very-smart"
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "http://maven.fabricmc.net")
}

version = "2.1.0+mc1.16.1"
group = "me.sargunvohra.mcmods"

minecraft {
}

dependencies {
    minecraft("com.mojang:minecraft:1.16.1")
    mappings("net.fabricmc:yarn:1.16.1+build.18:v2")
    modImplementation("net.fabricmc:fabric-loader:0.8.8+build.202")

    modImplementation("net.fabricmc.fabric-api:fabric-api:0.14.0+build.371-1.16")

    listOf(
        "me.shedaniel.cloth:config-2:4.5.6",
        "me.sargunvohra.mcmods:autoconfig1u:3.2.0-unstable"
    ).forEach {
        modImplementation(it)
        include(it)
    }

    modCompile("io.github.prospector:modmenu:1.12.2+build.17")
}

val processResources = tasks.getByName<ProcessResources>("processResources") {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        filter { line -> line.replace("%VERSION%", "${project.version}") }
    }
}

val javaCompile = tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val jar = tasks.getByName<Jar>("jar") {
    from("LICENSE")
}

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

curseforge {
    if (project.hasProperty("curseforge_api_key")) {
        apiKey = project.property("curseforge_api_key")!!
    }

    project(closureOf<CurseProject> {
        id = "318163"
        releaseType = "release"
        addGameVersion("1.16.1")
        addGameVersion("Fabric")
        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            embeddedLibrary("cloth-config")
            embeddedLibrary("auto-config-updated-api")
        })
        mainArtifact(file("${project.buildDir}/libs/${base.archivesBaseName}-$version.jar"))
        afterEvaluate {
            mainArtifact(remapJar)
            uploadTask.dependsOn(remapJar)
        }
    })

    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
