plugins {
    alias(libs.plugins.superrunner.jvm.library)
    alias(libs.plugins.superrunner.jvm.junit5)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)
    testImplementation(projects.core.test)
}