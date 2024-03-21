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
        maven {
            name =("ossrh-snapshot")

            url = uri("https://oss.sonatype.org/content/repositories/snapshots")

        }

        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name =("ossrh-snapshot")

            url = uri("https://oss.sonatype.org/content/repositories/snapshots")

        }

    }
}

rootProject.name = "MLKitCo"
include(":app")
 