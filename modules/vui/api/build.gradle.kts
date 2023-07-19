plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("iridium.mod.build-script")
    id("iridium.project.publish-script")
}

modSettings {
    modId("vui-api")
    modName("VUi: API")
    mixinFile("vui.mixins.json")
}

base.archivesName.set("vui-api")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
    implementation(dependencyHelper.modProject(":core"))
}

publishScript {
    repository("teamvoided", "https://maven.teamvoided.org/releases")
    publicationName("vui-api")
}