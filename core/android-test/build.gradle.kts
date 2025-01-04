plugins {
    alias(libs.plugins.superrunner.android.library)
    alias(libs.plugins.superrunner.android.junit5)
}

android {
    namespace = "com.eggdevs.core.android_test"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.auth.data)
    implementation(libs.coroutines.test)
    implementation(libs.bundles.ktor)
    implementation(libs.ktor.client.mock)

    api(projects.core.test)
}