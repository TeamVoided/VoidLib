plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-cresm")
	modName("VoidLib: CResM")
	entrypoint("main", "org.teamvoided.voidlib.cresm.CResM::onInitialize")
}

base.archivesName.set("voidlib-cresm")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}