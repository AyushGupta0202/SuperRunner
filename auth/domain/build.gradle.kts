plugins {
    alias(libs.plugins.superrunner.jvm.library)
    alias(libs.plugins.superrunner.jvm.junit5)
}

dependencies {
    implementation(projects.core.domain)
}