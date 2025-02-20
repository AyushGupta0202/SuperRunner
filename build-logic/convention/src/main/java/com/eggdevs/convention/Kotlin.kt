package com.eggdevs.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/* Configure:
* - Compile SDK.
* - Min SDK.
* - Java version.
* - Kotlin version
* - Desugaring */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
   commonExtension.apply {
       compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

       defaultConfig {
           minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()
       }

       compileOptions {
           isCoreLibraryDesugaringEnabled = true
           sourceCompatibility = JavaVersion.VERSION_11
           targetCompatibility = JavaVersion.VERSION_11
       }

       configureKotlin()

       dependencies {
           "coreLibraryDesugaring"(libs.findLibrary("desugar.jdk.libs").get())
       }
   }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    configureKotlin()
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

internal fun Project.configureTestLibraries(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    configureUnitTests(commonExtension)
    configureInstrumentationTests(commonExtension)
}

internal fun Project.configureUnitTests(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        dependencies {
            "testImplementation"(libs.findLibrary("androidx.junit").get())
        }
    }
}

internal fun Project.configureInstrumentationTests(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        dependencies {
            "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
            "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
        }
    }
}

internal fun DependencyHandlerScope.testDependency() {
    "testImplementation"(kotlin("test"))
}