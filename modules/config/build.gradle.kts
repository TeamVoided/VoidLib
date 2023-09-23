plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-config")
	modName("VoidLib: Config")
}

publishScript {
	repository("TeamVoided", "https://maven.teamvoided.org/releases")
	publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-config")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
	implementation(dependencyHelper.modProject(":vui"))
}
