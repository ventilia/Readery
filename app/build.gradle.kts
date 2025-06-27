plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
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
    // navigation
    implementation("androidx.navigation:navigation-fragment:2.8.1")
    implementation("androidx.navigation:navigation-ui:2.8.1")

    // ui & layouts
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("me.relex:circleindicator:2.1.6")

    // lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")

    // glide & gson
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.code.gson:gson:2.8.8")

    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    annotationProcessor("com.google.dagger:hilt-compiler:2.51.1")

    // firebase realtime db
    implementation("com.google.firebase:firebase-database:20.1.0")

    // http-клиент
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // pdf viewer с исключением старой библиотеки поддержки
    implementation("com.github.barteksc:AndroidPdfViewerV1:1.6.0") {
        exclude(group = "com.android.support", module = "support-v4")
    }


    // тесты
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}