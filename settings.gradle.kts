import java.util.Properties

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
    val gradleProperties = Properties()
    val gradlePropertiesFile = File(rootProject.projectDir, "gradle.properties")
    if (gradlePropertiesFile.exists()) {
        gradleProperties.load(gradlePropertiesFile.inputStream())
    }
    val androidxSnapshotBuildId = gradleProperties.getProperty("androidx.snapshotBuildId")
        ?: throw GradleException("Androidx snapshot build id must be defined in gradle.properties.")

    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://androidx.dev/snapshots/builds/$androidxSnapshotBuildId/artifacts/repository")
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }
}

rootProject.name = "Music"
include(":app")
include(":media")
include(":ui")
