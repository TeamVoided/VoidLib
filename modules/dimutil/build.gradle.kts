plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-dimutil")
	modName("VoidLib: DimUtil")
	mixinFile("dimutils.mixins.json")
	entrypoint("client", "org.teamvoided.voidlib.dimutil.DimUtil::onInitialize")
}

publishScript {
	releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
	publication(modSettings.modId(), isSnapshot = false)
	publication(modSettings.modId() + "Snapshot", isSnapshot = true)
}

base.archivesName.set("voidlib-dimutil")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}