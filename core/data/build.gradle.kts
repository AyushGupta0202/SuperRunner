plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.core.data"
}

dependencies {

    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.core.database)
}