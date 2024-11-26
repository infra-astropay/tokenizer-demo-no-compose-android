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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/astropay-it/tokenizer-android")
            credentials {
                username = System.getenv("SDK_ANDROID_USER_ARTIFACT")
                password = System.getenv("SDK_ANDROID_TOKEN_ARTIFACT")
            }
        }
    }
}

rootProject.name = "Pruebas No Compose"
include(":app")
 