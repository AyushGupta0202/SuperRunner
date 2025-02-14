plugins {
    alias(libs.plugins.superrunner.android.library.compose)
}

android {
    namespace = "com.eggdevs.core.presentation.ui"
}

dependencies {

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}