plugins {
    alias(libs.plugins.superrunner.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)
    testImplementation(projects.core.test)
}