plugins {
    alias(libs.plugins.superrunner.android.feature.ui)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "com.eggdevs.run.presentation"
}

dependencies {

    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.run.domain)
    implementation(projects.core.domain)
    implementation(projects.core.utils)
    implementation(projects.core.connectivity.domain)
}