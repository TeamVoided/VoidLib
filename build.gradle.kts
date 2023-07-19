plugins {
	id("fabric-loom") version "1.3.8"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.serialization") version "1.9.0"
	id("org.teamvoided.iridium") version "2.2.0"
	id("iridium.mod.build-script") version "2.2.0"
}

base.archivesName.set(project.properties["archives_base_name"] as String)
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

repositories {
	mavenCentral()
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
