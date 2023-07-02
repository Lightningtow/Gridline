//import com.google.protobuf.gradle.generateProtoTasks
//import com.google.protobuf.gradle.plugins
//import com.google.protobuf.gradle.protobuf
//import com.google.protobuf.gradle.protoc
import com.google.protobuf.gradle.*
import com.google.protobuf.*

plugins {
    id("com.android.application")
    id("kotlin-android")
//    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
//    id("com.google.protobuf")
    id("com.google.protobuf") version "0.8.12"

//    id ("com.android.application") version "7.2.1" apply false
//    id ("com.android.library") version "7.2.1" apply false
//    id ("org.jetbrains.kotlin.android") version "1.6.21" apply false
//    id ("org.jetbrains.kotlin.jvm") version "1.7.0" apply false
}

//sourceSets {
//    main {
//        java {
//            srcDirs = ['build/generated/source/proto/main/java']
//        }
//    }
//}
android {

    namespace = "com.lightningtow.gridline"
    compileSdk = 33
//    compileSdk = 31

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
//        /*
        release {
//            signingConfig signingConfigs.debug
//            minifyEnabled = false
//            isMinifyEnabled = true
// todo fix using debug keys

//            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt'), "proguard-rules.pro"
//            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"ecb84aef29dc414d97e133c1984b1e0d\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI_AUTH", "\"spotifyandroidplayground://spotify-auth\"")
            buildConfigField("String", "SPOTIFY_REDIRECT_URI_PKCE", "\"spotifyandroidplayground://spotify-pkce\"")
            signingConfig = signingConfigs.getByName("debug")
        }
//        */
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }

    composeOptions {
//        kotlinCompilerExtensionVersion = "1.1.1"
        kotlinCompilerExtensionVersion = "1.4.7"

    }
}

//var compose_version = "1.1.1"
var compose_version = "1.4.1"
var datastore_version = "1.1.0-alpha04"
var lifecycle_version = "2.4.1"
val protobuf_version = "3.21.7"

dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    implementation("com.google.protobuf:protobuf-javalite:$protobuf_version")

    implementation("androidx.datastore:datastore:$datastore_version")
    implementation("androidx.datastore:datastore-rxjava2:1.0.0")
    implementation("androidx.datastore:datastore-rxjava3:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")

    
    implementation("com.google.code.gson:gson:2.6.1")


//    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.lifecycle:lifecycle-common:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
//    implementation("android.arch.lifecycle:extensions:1.0.0")
//    annotationProcessor("android.arch.lifecycle:compiler:1.0.0")



//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10") // this seems to be duplicated within the sdk aar
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Images
    implementation("com.github.bumptech.glide:glide:4.13.1")
    implementation(files("libs/spotify-app-remote-release-0.7.2.aar"))
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.1")
    implementation("com.github.skydoves:landscapist-glide:1.5.0")

    // Spotify
    implementation("com.adamratzman:spotify-api-kotlin-android:3.8.6")

    implementation("androidx.compose.runtime:runtime:${compose_version}")

    implementation("androidx.compose.ui:ui:${compose_version}")
    implementation("androidx.compose.foundation:foundation-layout:${compose_version}")
    implementation("androidx.compose.material:material:${compose_version}")
    implementation("com.google.android.material:material:1.5.0")
//    implementation("androidx.compose.material:material-icons-core:${compose_version}")
//    implementation("androidx.compose.material:material-icons-extended:${compose_version}")
    implementation("androidx.compose.animation:animation:${compose_version}")
    implementation("androidx.compose.foundation:foundation:${compose_version}")
    implementation("androidx.compose.ui:ui-tooling-preview:${compose_version}")
    implementation("androidx.compose.runtime:runtime-livedata:${compose_version}")
    debugImplementation("androidx.compose.ui:ui-tooling:${compose_version}")

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
//  https://stackoverflow.com/questions/64811006/jetpack-proto-datastore-gradle-config-with-kotlin-dsl
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobuf_version"
    }
    generateProtoTasks {
        all().forEach { task ->
//            task.plugins{
//                create("java") {
//                    option("lite")
//                }
//            }
            task.plugins.create("java") {
                option("lite")
            }
        }
    }
}