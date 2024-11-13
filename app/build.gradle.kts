import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.barcode"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.barcode"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /*Hilt*/
    val daggerHiltVersion = "2.51"

    implementation("com.google.dagger:dagger:$daggerHiltVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerHiltVersion")
    api("com.google.dagger:dagger-android:$daggerHiltVersion")
    api("com.google.dagger:dagger-android-support:$daggerHiltVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerHiltVersion")
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

    //Logger: 로그 관련 lib
    implementation("com.orhanobut:logger:2.2.0")

    /*constraint layout*/
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // play service for code scanner
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") // 최신 버전 확인 후 적용

    }