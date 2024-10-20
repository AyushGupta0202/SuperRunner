plugins {
    alias(libs.plugins.superrunner.android.library.compose)
}

android {
    namespace = "com.eggdevs.wear.run.presentation"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {

    implementation(libs.androidx.wear.compose.ui.tooling)
    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.play.services.wearable)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin.compose)

    implementation(projects.core.presentation.designsystemWear)
}