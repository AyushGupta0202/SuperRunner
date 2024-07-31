plugins {
    alias(libs.plugins.superrunner.android.library)
    alias(libs.plugins.superrunner.jvm.ktor)
}

android {
    namespace = "com.eggdevs.auth.data"
}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.bundles.koin)
}