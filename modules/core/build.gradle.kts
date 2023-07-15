plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-core")
	modName("VoidLib: Core")
}

base.archivesName.set("voidlib-core")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String