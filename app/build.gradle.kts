import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
}

val projectInfo: ProjectInfo by lazy {
    return@lazy getVersionProperties()
}

android {
    compileSdkVersion(projectInfo.compileSdkVersion)

    defaultConfig {
        applicationId = "com.colin.go"
        minSdkVersion(projectInfo.minSdkVersion)
        targetSdkVersion(projectInfo.targetSdkVersion)
        versionCode = projectInfo.versionCode
        versionName = projectInfo.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}



data class ProjectInfo(
    val minSdkVersion: Int,
    val targetSdkVersion: Int,
    val compileSdkVersion: Int,
    val versionCode: Int,
    val versionName: String
)

fun getVersionProperties(): ProjectInfo {
    val properties = Properties()
    val input = File(projectDir.path + "/version.properties").inputStream()
    properties.load(input)
    input.close()
    return ProjectInfo(
        properties.getProperty("minSdkVersion").toInt(),
        properties.getProperty("targetSdkVersion").toInt(),
        properties.getProperty("compileSdkVersion").toInt(),
        properties.getProperty("versionCode").toInt(),
        properties.getProperty("versionName")
    )
}

dependencies {

    val lifecycleVersion = "2.2.0"
    val navVersion = "2.3.0"
    val roomVersion = "2.2.5"
    val retrofitVersion = "2.9.0"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    //lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")
    //room
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}