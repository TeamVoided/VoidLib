plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-config")
	modName("VoidLib: Config")

	entrypoint("main", "org.teamvoided.voidlib.config.impl.VoidFigImpl::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.config.impl.VoidFigImpl::clientSetup")
}

publishScript {
	releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
	publication(modSettings.modId(), isSnapshot = false)
	publication(modSettings.modId() + "Snapshot", isSnapshot = true)
}

base.archivesName.set("voidlib-config")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
	implementation(dependencyHelper.modProject(":vui"))
}
