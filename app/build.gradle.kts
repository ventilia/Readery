plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.readery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.readery"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.8.1")
    implementation("androidx.navigation:navigation-ui:2.8.1")

    // UI & Layouts
    implementation(libs.appcompat)
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("me.relex:circleindicator:2.1.6")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")

    // Glide & Gson
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.code.gson:gson:2.8.8")

    // Hilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    // Firebase Realtime DB
    implementation("com.google.firebase:firebase-database:20.1.0")

    // HTTP-клиент
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // **PDF Viewer** через JitPack (ветка v1.x)
    implementation("com.github.barteksc:AndroidPdfViewerV1:1.6.0")

    // Тесты
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
