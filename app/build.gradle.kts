plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.report.generator)
}

android {
    namespace = "com.mukas.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mukas.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val key =
            property("apikey")?.toString() ?: error(
                "You should add apikey into gradle.properties"
            )
        buildConfigField("String", "WEATHER_API_KEY", "\"$key\"")
    }

    buildTypes {
        debug {
            buildConfigField ("String", "BASE_URL", "\"https://api.weatherapi.com/v1/\"")
            buildConfigField ("String", "KEY_PARAM", "\"key\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField ("String", "BASE_URL", "\"https://api.weatherapi.com/v1/\"")
            buildConfigField ("String", "KEY_PARAM", "\"key\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir}/reports/compose",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir}/metrics/compose"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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
    implementation(libs.icons)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Compose navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    //Glide
    implementation(libs.glide.compose)

    //Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gsonConverter)
    implementation(libs.logging.interceptor)

    //Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    //Rebugger
    implementation(libs.rebugger)

    //Immutable Collections
    implementation(libs.kotlinx.collections.immutable)

    val version = "1.3.1"
    implementation("dev.shreyaspatil.compose-compiler-report-generator:core:$version")
    implementation("dev.shreyaspatil.compose-compiler-report-generator:report-generator:$version")
}