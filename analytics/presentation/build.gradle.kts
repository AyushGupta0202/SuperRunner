plugins {
    alias(libs.plugins.superrunner.android.feature.ui)
}

android {
    namespace = "com.eggdevs.analytics.presentation"
}

dependencies {

    implementation(projects.analytics.domain)
    implementation(libs.bundles.koin)
}