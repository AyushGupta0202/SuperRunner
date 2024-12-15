plugins {
    alias(libs.plugins.superrunner.android.library.compose)
}

android {
    namespace = "com.eggdevs.wear.presentation.ui"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {

    implementation(projects.core.presentation.ui)
    implementation(projects.wear.run.domain)
}