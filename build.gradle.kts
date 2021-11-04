import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.TaskModrinthUpload
import net.fabricmc.loom.task.RemapJarTask

val minecraftVersion: String by project
val parchmentVersion: String by project
val loaderVersion: String by project
val modId: String by project
val mavenGroup: String by project
val fabricVersion: String by project
val curseForgeId: String by project
val modrinthId: String by project
val modMenuVersion: String by project
val clothConfigVersion: String by project

plugins {
  java
  `maven-publish`
  id("fabric-loom") version "0.10.42"
  id("com.diffplug.spotless") version "5.17.0"
  id("org.jetbrains.changelog") version "1.3.1"
  id("com.github.jmongard.git-semver-plugin") version "0.4.2"
  id("com.matthewprenger.cursegradle") version "1.4.0"
  id("com.modrinth.minotaur") version "1.2.1"
}

buildscript { dependencies { classpath("org.openjdk.nashorn:nashorn-core:15.3") } }

group = mavenGroup

version =
    if (semver.version.contains('+')) {
      "${semver.version}.mc$minecraftVersion"
    } else {
      "${semver.version}+mc$minecraftVersion"
    }

sourceSets {
  create("testmod") {
    compileClasspath += main.get().compileClasspath
    runtimeClasspath += main.get().runtimeClasspath
  }
}

loom {
  runs {
    create("testmodClient") {
      client()
      name("Testmod Client")
      ideConfigGenerated(true)
      source(sourceSets.getByName("testmod"))
    }
    create("gametest") {
      server()
      name("Game Test")
      ideConfigGenerated(true)
      source(sourceSets.getByName("testmod"))
      vmArg("-Dfabric-api.gametest")
      runDir("build/gametest")
    }
  }
}

tasks.getByName("test").dependsOn("runGametest")

repositories {
  // Loom adds the essential maven repositories automatically.
  maven(url = "https://maven.terraformersmc.com/")
  maven(url = "https://maven.parchmentmc.net/")
  maven(url = "https://maven.shedaniel.me/")
}

dependencies {
  minecraft("com.mojang:minecraft:$minecraftVersion")
  mappings(
      loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$minecraftVersion:$parchmentVersion@zip")
      })

  this.add("testmodImplementation", sourceSets.main.get().output)

  // remember to update the dependency list in fabric.mod.json and the curseforge block below
  modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
  modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

  modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")
  include("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion")

  modRuntimeOnly("com.terraformersmc:modmenu:$modMenuVersion")
  modCompileOnly("com.terraformersmc:modmenu:$modMenuVersion")
}

base { archivesName.set(modId) }

java {
  sourceCompatibility = JavaVersion.VERSION_16
  targetCompatibility = JavaVersion.VERSION_16
  withSourcesJar()
}

tasks.getByName<ProcessResources>("processResources") {
  filteringCharset = "UTF-8"

  inputs.property("project.version", project.version)
  inputs.property("project.properties", project.properties.toString())

  filesMatching("fabric.mod.json") {
    expand(project.properties + mapOf("modVersion" to "${project.version}"))
  }
}

val javaCompile = tasks.withType<JavaCompile> { options.encoding = "UTF-8" }

val jar = tasks.getByName<Jar>("jar") { from("LICENSE") }

val remapJar = tasks.getByName<RemapJarTask>("remapJar")

spotless {
  java {
    importOrder()
    prettier(mapOf("prettier" to "2.4.1", "prettier-plugin-java" to "1.5.0"))
  }

  kotlinGradle { ktfmt() }

  freshmark {
    target("**/*.md")
    propertiesFile("gradle.properties")
    prettier()
  }

  format("misc") {
    target("**/*.json", "**/*.yml")
    prettier()
  }
}

changelog {
  version.set(semver.version)
  groups.set(listOf("Changes"))
  unreleasedTerm.set("Current")
  header.set(
      provider {
        if (!semver.semVersion.isSnapshot) version.get()
        else throw Exception("Can't patch changelog of snapshot version")
      })
}

val changelogText = changelog.getLatest().toText()

tasks.getByName("patchChangelog").finalizedBy(tasks.getByName("spotlessFreshmarkApply"))

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifact("remapJar") { builtBy(tasks.getByName("remapJar")) }
      artifact("sourcesJar") { builtBy(tasks.getByName("remapSourcesJar")) }
    }
  }
  repositories {
    // Add repositories to publish to here.
  }
}

if (project.hasProperty("curseforge_token")) {
  curseforge {
    apiKey = project.property("curseforge_token")

    project(
        closureOf<CurseProject> {
          id = curseForgeId
          changelog = changelogText
          releaseType = "release"
          addGameVersion(minecraftVersion)
          addGameVersion("Fabric")
          relations(
              closureOf<CurseRelation> {
                requiredDependency("fabric-api")
                embeddedLibrary("cloth-config")
              })
          mainArtifact(remapJar.archiveFile)
          afterEvaluate { uploadTask.dependsOn(remapJar) }
        })

    options(
        closureOf<Options> {
          forgeGradleIntegration = false
          detectNewerJava = true
          debug = semver.semVersion.isSnapshot
        })
  }
}

if (project.hasProperty("modrinth_token")) {
  task("modrinth", TaskModrinthUpload::class) {
        token = "${project.property("modrinth_token")}"
        projectId = modrinthId
        versionNumber = "${project.version}"
        changelog = changelogText
        uploadFile = remapJar
        addGameVersion(minecraftVersion)
        addLoader("fabric")
      }
      .dependsOn(remapJar)
}
