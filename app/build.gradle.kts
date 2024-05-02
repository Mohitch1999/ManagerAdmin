plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.managerapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.managerapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.android.volley:volley:1.2.1")


    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("androidx.media3:media3-exoplayer:1.3.0")
    implementation("androidx.media3:media3-ui:1.3.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("androidx.camera:camera-core:1.3.3")
    kapt("com.google.dagger:hilt-android-compiler:2.44")


    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    implementation ("androidx.work:work-runtime:2.9.0")

    implementation ("pub.devrel:easypermissions:3.0.0")

    // dependency for exoplayer

    implementation ("com.google.android.exoplayer:exoplayer:r2.4.0")

// for core support in exoplayer.

    implementation ("com.google.android.exoplayer:exoplayer-core:r2.4.0")


    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation ("com.iceteck.silicompressorr:silicompressor:2.2.4")
    implementation ("com.googlecode.mp4parser:isoparser:1.1.22")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}