plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.run.network"
}

dependencies {
    implementation(projects.run.domain)
    implementation(projects.core.domain)
}