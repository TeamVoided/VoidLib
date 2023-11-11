import org.gradle.configurationcache.extensions.capitalized

plugins {
	id("fabric-loom") version "1.3.8"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.serialization") version "1.9.0"
	id("org.teamvoided.iridium") version "3.1.9"
}

base.archivesName.set(project.properties["archives_base_name"] as String)
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

repositories {
	mavenCentral()
}

modSettings {
	modId(base.archivesName.get())
	modName(base.archivesName.get().capitalized())
}

loom {
	runs {
		create("vuiVisualEditor") {
			client()
			vmArg("-Dvuieditor")
			vmArg("-Dvuistopmusic")
		}
	}
}

dependencies {
	implementation(dependencyHelper.modProject(":tests"))
}

tasks {
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
