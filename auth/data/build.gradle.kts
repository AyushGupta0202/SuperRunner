plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.auth.data"
}

dependencies {

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}