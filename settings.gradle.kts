pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

include(
    "config",
    "core",
    "cresm",
    "dimutil",
    "pow",
    "vui",
    "wfc"
)

project(":config").projectDir = file("modules/config")
project(":core").projectDir = file("modules/core")
project(":cresm").projectDir = file("modules/cresm")
project(":dimutil").projectDir = file("modules/dimutil")
project(":pow").projectDir = file("modules/pow")
project(":vui").projectDir = file("modules/vui")
project(":wfc").projectDir = file("modules/wfc")
