plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-dimutil")
	modName("VoidLib: DimUtil")
	mixinFile("dimutil.mixins.json")
	entrypoint("client", "org.teamvoided.voidlib.dimutil.DimUtil")
}

base.archivesName.set("voidlib-dimutil")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
	implementation(dependencyHelper.modProject(":core"))
}