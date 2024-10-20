plugins {
    alias(libs.plugins.superrunner.android.library.compose)
}

android {
    namespace = "com.eggdevs.core.presentation.designsystem_wear"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {

    api(projects.core.presentation.designsystem)
    implementation(libs.androidx.wear.compose.material)
}