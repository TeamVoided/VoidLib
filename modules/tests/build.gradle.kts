plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("iridium.mod.build-script")
}

modSettings {
    modId("voidlib-tests")
    modName("VoidLib: Tests")

    entrypoint("main", "org.teamvoided.voidlib.tests.Tests::commonSetup")
    entrypoint("client", "org.teamvoided.voidlib.tests.Tests::clientSetup")
}

publishScript {
    repository("TeamVoided", "https://maven.teamvoided.org/releases")
    publicationName(modSettings.modId())
}

base.archivesName.set("voidlib-tests")
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