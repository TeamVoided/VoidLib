# VoidLib

## The common library used for Team Voided mods
#### Includes an implementation of the Wave Function Collapse algorithm (originally by [me](https://github.com/BrokenFuse), still by [me](https://github.com/BrokenFuse))

<br>
<br>

<details>
<summary>Gradle Dependency</summary>

```kotlin
repositiories {
    maven("https://maven.brokenfuse.me/releases")
    //projects will eventually be migrated to https://maven.teamvoided.org
    //as of right now https://maven.teamvoided.org just points to https://maven.brokenfuse.me
}
```

```kotlin
dependencies {
    modImplementation("org.teamvoided:voidlib:${project.properties["voidlib_version"]}")
    //latest 1.4.0+1.19.3
    
    /*
    * For versions older than 1.4.0+1.19.3 use the following
    * modImplementation("org.team.voided:voidlib:${project.properties["voidlib_version"]}")
    */
}
```

<h3>For usage details goto the wiki!!</h3>
</details>

<details>
<summary>Versioning</summary>

## Scheme \<MAJOR>.\<MINOR>.\<PATCH>+<MC_VERSION>

## Patch
Increment in patch: Same content, external apis & networking protocol, (server & client can have different patch versions)

## Minor
Increment in minor: Gameplay/Content changes & api methods added but none removed (Deprecation allowed) (server & client must have the same minor version)

## Major
Increment in major: World may corrupt on mod update, Gameplay/Content changes & api methods added and removed (server & client must have the same major version)

## Mc Version
The version of minecraft the mod is built against

<br>
(THIS IS MY PREFERENCE AND NOT ADVICE ON HOW TO VERSION THINGS)

</details>
