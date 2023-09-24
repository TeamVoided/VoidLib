pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.teamvoided.org/releases")
    }
}

include(
    "config",
    "core",
    "cresm",
    "dimutil",
    "networking",
    "pow",
    "vui",
    "wfc",
    "woodset",
    "tests"
)

project(":config").projectDir = file("modules/config")
project(":core").projectDir = file("modules/core")
project(":cresm").projectDir = file("modules/cresm")
project(":dimutil").projectDir = file("modules/dimutil")
project(":networking").projectDir = file("modules/networking")
project(":pow").projectDir = file("modules/pow")
project(":vui").projectDir = file("modules/vui")
project(":wfc").projectDir = file("modules/wfc")
project(":woodset").projectDir = file("modules/woodset")

project(":tests").projectDir = file("modules/tests")
