plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-wfc")
	modName("VoidLib: WFC")
	entrypoint("main", "org.teamvoided.voidlib.wfc.WFC::onInitialize")
}

base.archivesName.set("voidlib-wfc")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
	implementation(dependencyHelper.modProject(":cresm"))
}