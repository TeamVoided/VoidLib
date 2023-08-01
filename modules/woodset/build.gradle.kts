plugins {
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("iridium.mod.build-script")
}

modSettings {
	modId("voidlib-woodset")
	modName("VoidLib: WoodSet")
	mixinFile("woodset.mixins.json")


	entrypoint("main", "org.teamvoided.voidlib.woodset.WoodSet::commonSetup")
	entrypoint("client", "org.teamvoided.voidlib.woodset.WoodSet::clientSetup")
}

base.archivesName.set("voidlib-woodset")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

repositories {
	maven { url = uri("https://jitpack.io") }
}

dependencies {
	//Temp while I figure out how to sign
	implementation(include(annotationProcessor("com.github.llamalad7.mixinextras:mixinextras-fabric:0.2.0-beta.9")!!)!!)
}