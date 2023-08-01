plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-pow")
	modName("VoidLib: Pow")
	mixinFile("pow.mixins.json")
	entrypoint("main", "org.teamvoided.voidlib.pow.Pow::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.pow.Pow::clientSetup")
}

publishScript {
	repository("TeamVoided", "https://maven.teamvoided.org/releases")
	publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-pow")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}