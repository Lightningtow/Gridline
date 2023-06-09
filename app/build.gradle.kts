plugins {
    id("com.android.application")
    id("kotlin-android")
//    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"


//    id ("com.android.application") version "7.2.1" apply false
//    id ("com.android.library") version "7.2.1" apply false
//    id ("org.jetbrains.kotlin.android") version "1.6.21" apply false
//    id ("org.jetbrains.kotlin.jvm") version "1.7.0" apply false
}

android {
    namespace = "com.lightningtow.gridline"
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.lightningtow.gridline"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
//            storePassword = property("keystorePassword") as String
            keyAlias = "androiddebugkey"
//            keyPassword = property("debugKeyPassword") as String
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true

    }
    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"ecb84aef29dc414d97e133c1984b1e0d\"")
            buildConfigField(
                "String",
                "SPOTIFY_REDIRECT_URI_AUTH",
                "\"gridline://spotify-auth\""
            )
            buildConfigField(
                "String",
                "SPOTIFY_REDIRECT_URI_PKCE",
                "\"gridline://spotify-pkce\""
            )

        }
        /*release {
            minifyEnabled = false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt'), "proguard-rules.pro"
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"0d7afea9e025418892dd31674b6170ee\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI_AUTH", "\"spotifyandroidplayground://spotify-auth\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI_PKCE", "\"spotifyandroidplayground://spotify-pkce\"")
        }*/
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
//        kotlinCompilerExtensionVersion = "1.2.0"

    }
}
var composeVersion = "1.1.1"

dependencies {
//    implementation project(':spotify-app-remote')
    implementation("com.google.code.gson:gson:2.6.1")


    implementation("androidx.datastore:datastore-preferences:1.0.0")

//    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")

//    implementation("androidx.compose.material3:material3:1.1.0")
//    implementation("org.greenrobot:eventbus:3.3.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Images
    implementation("com.github.bumptech.glide:glide:4.13.1")
    implementation(files("libs/spotify-app-remote-release-0.7.2.aar"))
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.1")
    implementation("com.github.skydoves:landscapist-glide:1.5.0")

    // Spotify
    implementation("com.adamratzman:spotify-api-kotlin-android:3.8.6")

    implementation("androidx.compose.runtime:runtime:${composeVersion}")
//    implementation("androidx.compose.runtime:runtime:${composeVersion}")

    implementation("androidx.compose.ui:ui:${composeVersion}")
    implementation("androidx.compose.foundation:foundation-layout:${composeVersion}")
    implementation("androidx.compose.material:material:${composeVersion}")
    implementation("com.google.android.material:material:1.5.0")
//    implementation("androidx.compose.material:material-icons-core:${composeVersion}")
//    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
    implementation("androidx.compose.foundation:foundation:${composeVersion}")
    implementation("androidx.compose.animation:animation:${composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${composeVersion}")
    implementation("androidx.compose.runtime:runtime-livedata:${composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${composeVersion}")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.activity:activity-compose:1.4.0")

    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.window:window:1.0.0")


    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
/*
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")

    implementation("androidx.compose.runtime:runtime:${composeVersion}")
    implementation("androidx.compose.ui:ui:${composeVersion}")
    implementation("androidx.compose.foundation:foundation-layout:${composeVersion}")
    implementation("androidx.compose.material:material:${composeVersion}")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.compose.material:material-icons-core:${composeVersion}")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
    implementation("androidx.compose.foundation:foundation:${composeVersion}")
    implementation("androidx.compose.animation:animation:${composeVersion}")
    implementation("androidx.compose.ui:ui-tooling-preview:${composeVersion}")
    implementation("androidx.compose.runtime:runtime-livedata:${composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${composeVersion}")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.23.1")

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.activity:activity-compose:1.4.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")

    implementation("androidx.navigation:navigation-compose:2.4.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
 */