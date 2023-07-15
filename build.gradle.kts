plugins {
	id("fabric-loom") version "1.3.8"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.serialization") version "1.9.0"
	id("org.teamvoided.iridium") version "1.3.1"
}

base.archivesName.set(project.properties["archives_base_name"] as String)
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

repositories {
	mavenCentral()
	maven("https://maven.quiltmc.org/repository/release")
}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("org.quiltmc:quilt-mappings:1.19.4+build.10:intermediary-v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
}

tasks {
	processResources {
		inputs.property("version", project.version)
		filteringCharset = "UTF-8"

		filesMatching("fabric.mod.json") {
			expand(mapOf("version" to project.version))
		}
	}

	val targetJavaVersion = 17
	withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(targetJavaVersion)
	}

	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions.jvmTarget = targetJavaVersion.toString()
	}

	java {
		toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
		withSourcesJar()
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${base.archivesName.get()}" }
		}
	}
}
