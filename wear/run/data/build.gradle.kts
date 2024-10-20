plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.wear.run.data"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {

    implementation(libs.bundles.koin)
    implementation(libs.androidx.health.services.client)
}