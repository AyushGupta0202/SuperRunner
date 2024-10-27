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

    implementation(projects.wear.run.domain)

    implementation(libs.bundles.koin)
    implementation(libs.androidx.health.services.client)

    implementation(projects.core.connectivity.domain)
}