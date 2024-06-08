plugins {
    alias(libs.plugins.superrunner.android.feature.ui)
}

android {
    namespace = "com.eggdevs.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
}