plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("iridium.mod.build-script")
}

modSettings {
    modId("void_test")
    modName("VoidLib: Tests")

    entrypoint("main", "org.teamvoided.voidlib.tests.Tests::commonSetup")
    entrypoint("client", "org.teamvoided.voidlib.tests.Tests::clientSetup")
    entrypoint("fabric-datagen", "org.teamvoided.voidlib.tests.TestsData")
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


loom {
    runs {
        create("data") {
            client()
            ideConfigGenerated(true)
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=${"void_test"}")
            runDir("build/datagen")
        }
    }
}

sourceSets["main"].resources.srcDir("src/main/generated")
