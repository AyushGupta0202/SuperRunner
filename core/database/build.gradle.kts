plugins {
    alias(libs.plugins.superrunner.android.library)
}

android {
    namespace = "com.eggdevs.core.database"
}

dependencies {

    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}