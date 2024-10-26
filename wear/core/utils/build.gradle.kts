plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.wear.core.utils"
    compileSdk = 34

    defaultConfig {
        minSdk = 30
    }
}

dependencies {

    api(projects.core.utils)
}