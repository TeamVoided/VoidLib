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
	repository("TeamVoided", "https://maven.teamvoided.org/releases")
	publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-vui")
version = "0.1.0-BETA"
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}