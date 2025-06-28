pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.1.0" apply false
        id("com.google.gms.google-services") version "4.3.15" apply false
        id("com.google.dagger.hilt.android") version "2.51.1" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://repository.liferay.com/nexus/content/repositories/public/") } // для AndroidPdfViewerV1
    }
}

rootProject.name = "readery"
include(":app")