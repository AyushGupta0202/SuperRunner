plugins {
    alias(libs.plugins.superrunner.android.application.wear.compose)
}

android {
    namespace = "com.eggdevs.wear.app"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.koin.compose)

    implementation(projects.core.presentation.designsystemWear)
}