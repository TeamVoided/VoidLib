plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-vui")
	modName("VoidLib: VUi")
	mixinFile("vui.mixins.json")
	entrypoint("main", "org.teamvoided.voidlib.vui.impl.Vui::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.vui.impl.Vui::clientSetup")
}

base.archivesName.set("voidlib-vui")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
	implementation(dependencyHelper.jarInclude(":vui:api"))
}