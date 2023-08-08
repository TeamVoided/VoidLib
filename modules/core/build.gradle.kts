plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

modSettings {
	modId("voidlib-core")
	modName("VoidLib: Core")
}

publishScript {
	repository("TeamVoided", "https://maven.teamvoided.org/releases")
	publicationName(modSettings.modId())
}

dependencies {
	implementation(shadow("net.benwoodworth.knbt:knbt:0.11.3")!!)
}

tasks {
	shadowJar {
		destinationDirectory.set(buildDir.resolve("devlibs"))
		archiveClassifier.set("dev")
		configurations = mutableListOf(project.configurations.shadow.get() as FileCollection)
		exclude {
			it.path.startsWith("org/intellij") ||
					it.path.startsWith("org/jetbrains") ||
					it.path.startsWith("kotlinx") ||
					it.path.startsWith("kotlin")
		}

		relocate("net.benwoodworth.knbt", "org.teamvoided.voidlib.core.nbt")
		relocate("okio", "org.teamvoided.voidlib.core.okio")
	}

	remapJar {
		dependsOn(shadowJar)
		mustRunAfter(shadowJar)
		inputFile.set(shadowJar.get().archiveFile)
	}
}

base.archivesName.set("voidlib-core")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String