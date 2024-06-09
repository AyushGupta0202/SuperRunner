plugins {
    alias(libs.plugins.superrunner.android.library)
    alias(libs.plugins.superrunner.jvm.ktor)
}

android {
    namespace = "com.eggdevs.run.network"
}

dependencies {
    implementation(projects.run.domain)
    implementation(projects.core.domain)
}