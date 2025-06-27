pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        // версии плагинов не менялись
        alias(libs.plugins.android.application) version "x.y.z" apply false
        alias(libs.plugins.hilt.android)       version "x.y.z" apply false
        id("com.google.gms.google-services")   version "4.3.15" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // ← добавили JitPack, чтобы подтягивать AndroidPdfViewerV1
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "readery"
include(":app")