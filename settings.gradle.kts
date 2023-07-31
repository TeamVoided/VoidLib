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
    "pow",
    "vl-all",
    "vui",
    "vui:api",
    "wfc",
    "woodset"
)

project(":config").projectDir = file("modules/config")
project(":core").projectDir = file("modules/core")
project(":cresm").projectDir = file("modules/cresm")
project(":dimutil").projectDir = file("modules/dimutil")
project(":pow").projectDir = file("modules/pow")
project(":vl-all").projectDir = file("modules/vl-all")
project(":vui").projectDir = file("modules/vui")
project(":vui:api").projectDir = file("modules/vui/api")
project(":wfc").projectDir = file("modules/wfc")
project(":woodset").projectDir = file("modules/woodset")
