plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "com.chad.baserecyclerviewadapterhelper"
        minSdk = 23
        targetSdk = 36
        versionCode = 21
        versionName = "4.3.2"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    namespace = "com.chad.baserecyclerviewadapterhelper"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar", "*.aar"), "dir" to "libs")))
    implementation(project(":library"))
    implementation(libs.google.material)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.kyleduo.switchbutton)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.jaredrummler.material.spinner)
}
