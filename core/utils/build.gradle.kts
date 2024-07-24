plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.core.utils"
}

dependencies {

    implementation(projects.core.domain)
}