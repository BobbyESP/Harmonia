plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-parcelize")
    id("dev.shreyaspatil.compose-compiler-report-generator") version "1.1.0"
}

android {
    namespace = "com.kyant.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
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
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi," +
                "kotlinx.serialization.ExperimentalSerializationApi," +
                "androidx.compose.animation.ExperimentalAnimationApi," +
                "androidx.compose.animation.core.ExperimentalTransitionApi," +
                "androidx.compose.foundation.ExperimentalFoundationApi," +
                "androidx.compose.foundation.layout.ExperimentalLayoutApi," +
                "androidx.compose.ui.ExperimentalComposeUiApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(libs.core.ktx)
    api(libs.activity.compose)
    api(libs.ui)
    api(libs.ui.graphics)
    api(libs.material3)
    api("com.github.Kyant0:m3color:2023.10.1") // https://github.com/Kyant0/m3color
}
