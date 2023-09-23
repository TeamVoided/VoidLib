plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("iridium.mod.build-script")
    id("iridium.project.publish-script")
}

modSettings {
    modId("voidlib-config")
    modName("VoidLib: Config")

    entrypoint("main", "org.teamvoided.voidlib.tests.Tests::commonSetup")
    entrypoint("client", "org.teamvoided.voidlib.tests.Tests::clientSetup")
}

publishScript {
    repository("TeamVoided", "https://maven.teamvoided.org/releases")
    publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-config")
version = project.properties["mod_version"] as String
group = project.properties["maven_group"] as String

dependencies {
    implementation(dependencyHelper.modProject(":config"))
    implementation(dependencyHelper.modProject(":core"))
    implementation(dependencyHelper.modProject(":cresm"))
    implementation(dependencyHelper.modProject(":dimutil"))
    implementation(dependencyHelper.modProject(":pow"))
    implementation(dependencyHelper.modProject(":vui"))
    implementation(dependencyHelper.modProject(":wfc"))
    implementation(dependencyHelper.modProject(":woodset"))
}