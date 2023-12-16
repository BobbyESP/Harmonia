pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://androidx.dev/snapshots/builds/11228099/artifacts/repository")
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }
}

rootProject.name = "Music"
include(":app")
include(":media")
include(":ui")
