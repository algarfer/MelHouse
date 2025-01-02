plugins {
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.uniovi.melhouse"
    compileSdk = 34

    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
    defaultConfig {
        applicationId = "com.uniovi.melhouse"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.uniovi.melhouse.MyTestRunner"
    }

    buildTypes {
        val supabaseDevPort = 8000
        val supabaseDevHost = "10.0.2.2"
        val supabaseDeployPort = 8000
        val supabaseDeployHost = "158.179.219.235"

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "SUPABASE_URL", "\"http://$supabaseDeployHost:$supabaseDeployPort\"")
            buildConfigField("int", "SUPABASE_PORT", supabaseDeployPort.toString())
            buildConfigField("String", "SUPABASE_HOST", "\"$supabaseDeployHost\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ewogICJyb2xlIjogImFub24iLAogICJpc3MiOiAiTWVsaG91c2UtU3VwYWJhc2UiLAogICJpYXQiOiAxNzMxMTA2ODAwLAogICJleHAiOiAxODg4ODczMjAwCn0.MTWFEMifuvTyyUhjdlCam0EQWDuZKoe_4iHclRrhtYk\"")
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            buildConfigField("String", "SUPABASE_URL", "\"http://$supabaseDevHost:$supabaseDevPort\"")
            buildConfigField("int", "SUPABASE_PORT", supabaseDevPort.toString())
            buildConfigField("String", "SUPABASE_HOST", "\"$supabaseDevHost\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgCiAgICAicm9sZSI6ICJhbm9uIiwKICAgICJpc3MiOiAic3VwYWJhc2UtZGVtbyIsCiAgICAiaWF0IjogMTY0MTc2OTIwMCwKICAgICJleHAiOiAxNzk5NTM1NjAwCn0.dc_X5iR_VP_qT0zsiyj_I_OZ2T9FtRU2BBNWN8Bu4GE\"")
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.agent)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.hilt.android.test)

    implementation(libs.calendar)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    implementation(libs.zxing.core)
    implementation(libs.zxing.android.embedded)
    kapt(libs.hilt.android.compiler)
    implementation(platform(libs.bom))
    implementation(libs.postgrest.kt)
    implementation(libs.auth.kt)
    implementation(libs.realtime.kt)
    implementation(libs.ktor.client.cio)

    implementation(libs.mpandroidchart)
}

kapt {
    correctErrorTypes = true
}

