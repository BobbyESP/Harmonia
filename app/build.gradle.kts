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
                "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi," +
                "androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi," +
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
    implementation(libs.androidx.core)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.windowsizeclass)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kyant.taglib)
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01") // https://github.com/bumptech/glide
    implementation("com.github.GIGAMOLE:ComposeFadingEdges:1.0.4") // https://github.com/GIGAMOLE/ComposeFadingEdges
    implementation("com.touchlane:gridpad:1.1.2") // https://github.com/touchlane/gridpad-android
    implementation("dev.chrisbanes.haze:haze-jetpack-compose:0.3.1") // https://github.com/chrisbanes/haze
}
