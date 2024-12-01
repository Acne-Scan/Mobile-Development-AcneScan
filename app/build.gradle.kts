plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id ("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.dicoding.acnescan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dicoding.acnescan"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BaseUrlIdentification", "\"http://18.141.72.15/\"")
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
        mlModelBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    //kamera
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)

    //camerax
    implementation (libs.camera.core)
    implementation (libs.camera.camera2)
    implementation (libs.camera.lifecycle)
    implementation (libs.camera.view)

    //retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)//untuk lifecycleScope

    //tflite
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)

    //room
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    kapt ("androidx.room:room-compiler:2.6.0") // Jika menggunakan Kotlin

    //coroutine
    implementation (libs.kotlinx.coroutines.android)

}