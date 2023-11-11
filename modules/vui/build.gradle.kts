plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
	id("iridium.project.publish-script")
}

modSettings {
	modId("voidlib-vui")
	modName("VoidLib: VUi")
	mixinFile("vui.mixins.json")
	entrypoint("main", "org.teamvoided.voidlib.vui.impl.Vui::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.vui.impl.Vui::clientSetup")
}

publishScript {
	releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
	publication(modSettings.modId(), isSnapshot = false)
	publication(modSettings.modId() + "Snapshot", isSnapshot = true)
}

base.archivesName.set("voidlib-vui")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}