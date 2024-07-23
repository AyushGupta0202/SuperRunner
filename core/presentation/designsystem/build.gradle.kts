plugins {
    alias(libs.plugins.superrunner.android.library.compose)
}

android {
    namespace = "com.eggdevs.core.presentation.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(projects.core.utils)
    debugImplementation(libs.androidx.ui.tooling)
    api(libs.androidx.material3)
}