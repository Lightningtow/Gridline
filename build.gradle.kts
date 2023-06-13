import com.google.protobuf.gradle.protobuf

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")

        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.12")

//        classpath("com.android.tools.build:gradle:7.4.2")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
