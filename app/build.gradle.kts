import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.protobuf)
}

android {

    namespace = "meow.softer.yuyuan"
    compileSdk = 36

    defaultConfig {
        applicationId = "meow.softer.yuyuan"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "0.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// compose compiler options
// reference: https://kotlinlang.org/docs/compose-compiler-options.html
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

// might have lint warns
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.30.2"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

// Set Room Schema export location
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // compose bill of material
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    testImplementation(composeBom)
    androidTestImplementation(composeBom)
    debugImplementation(composeBom)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.activity.ktx)
    implementation(libs.constraintlayout.compose)

    implementation(libs.bundles.compose.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Splash Screen
    implementation(libs.core.splashscreen)
    // Navigation
    implementation(libs.navigation.compose)
    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.hilt.android.compiler)
    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    // optional - Test helpers
    testImplementation(libs.room.testing)
    // DataStore
    implementation(libs.datastore)
    implementation(libs.protobuf.javalite)
    // ExoPlayer
    implementation(libs.bundles.media3)
    // desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}