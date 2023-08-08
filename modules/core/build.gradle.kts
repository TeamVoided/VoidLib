plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-core")
	modName("VoidLib: Core")
}

publishScript {
	repository("TeamVoided", "https://maven.teamvoided.org/releases")
	publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-core")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String