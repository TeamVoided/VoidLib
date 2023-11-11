plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-cresm")
	modName("VoidLib: CResM")
	entrypoint("main", "org.teamvoided.voidlib.cresm.CResM::onInitialize")
}

publishScript {
	releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
	publication(modSettings.modId(), isSnapshot = false)
	publication(modSettings.modId() + "Snapshot", isSnapshot = true)
}

base.archivesName.set("voidlib-cresm")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}