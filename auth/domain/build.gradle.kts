plugins {
    alias(libs.plugins.superrunner.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}