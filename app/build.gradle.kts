plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    namespace = "com.dam.armoniaskills"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dam.armoniaskills"
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
}

dependencies {

    implementation(libs.java.websocket)
    implementation(libs.gson)
    implementation(libs.palette)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.firebase.messaging)
    implementation(platform(libs.firebase.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.glide)
    implementation(libs.imageslideshow)
    implementation(libs.okhttp)

}