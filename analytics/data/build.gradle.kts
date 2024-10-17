plugins {
    alias(libs.plugins.superrunner.android.library)
    alias(libs.plugins.superrunner.android.room)
}

android {
    namespace = "com.eggdevs.analytics.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.analytics.domain)
}