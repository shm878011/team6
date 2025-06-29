import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.secrets.gradle.plugin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.team6"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.team6"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            "\"${project.findProperty("NAVER_CLIENT_ID") ?: "DEFAULT_CLIENT_ID"}\""
        )
        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            "\"${project.findProperty("NAVER_CLIENT_SECRET") ?: "DEFAULT_CLIENT_SECRET"}\""
        )



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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation("com.naver.maps:map-sdk:3.21.0")
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("com.google.android.gms:play-services-safetynet:18.0.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.google.accompanist:accompanist-webview:0.28.0")
}

secrets{
    propertiesFileName= "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}