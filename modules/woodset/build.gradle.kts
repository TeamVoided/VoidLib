plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

base.archivesName.set("voidlib-woodset")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

modSettings {
	modId("voidlib-woodset")
	modName("VoidLib: WoodSet")

	entrypoint("main", "org.teamvoided.voidlib.woodset.WoodSet::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.woodset.WoodSet::clientSetup")
	mixinFile("woodset.mixins.json")
}

publishScript {
	releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
	publication(modSettings.modId(), isSnapshot = false)
	publication(modSettings.modId() + "Snapshot", isSnapshot = true)
}

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}
