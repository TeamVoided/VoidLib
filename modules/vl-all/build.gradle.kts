plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("iridium.mod.build-script")
    id("iridium.mod.jar-in-jar")
}

base.archivesName.set("voidlib-all")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

modSettings {
    modId("voidlib-all")
    modName("VoidLib")
}

loom {
    runs {
        create("vuiVisualEditor") {
            client()
            vmArg("-Dvuieditor")
            vmArg("-Dvuistopmusic")
        }
    }
}

fun DependencyHandler.modProject(path: String) =
    project(path, configuration = "namedElements")