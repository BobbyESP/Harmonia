plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    id("dev.shreyaspatil.compose-compiler-report-generator") version "1.1.0"
}

android {
    namespace = "com.kyant.music"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kyant.music"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0.a1"
        ndk {
            abiFilters += arrayOf("arm64-v8a", "armeabi-v7a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        languageVersion = "2.0"
        freeCompilerArgs += arrayOf(
            "-Xcontext-receivers",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi," +
                "kotlinx.serialization.ExperimentalSerializationApi," +
                "androidx.compose.animation.core.ExperimentalTransitionApi," +
                "androidx.compose.foundation.ExperimentalFoundationApi," +
                "androidx.compose.foundation.layout.ExperimentalLayoutApi," +
                "androidx.compose.material3.ExperimentalMaterial3Api," +
                "androidx.compose.ui.ExperimentalComposeUiApi," +
                "com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += arrayOf("/META-INF/{AL2.0,LGPL2.1}", "DebugProbesKt.bin")
        }
        dex {
            useLegacyPackaging = true
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }
    lint {
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":media"))
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.core.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.material.icons.extended)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.glide.compose) // https://github.com/bumptech/glide
    implementation(libs.kyant.taglib) // https://github.com/Kyant0/taglib
    implementation("com.touchlane:gridpad:1.1.2") // https://github.com/touchlane/gridpad-android
}
