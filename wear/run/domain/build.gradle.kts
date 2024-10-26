plugins {
    alias(libs.plugins.superrunner.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    api(projects.core.domain) // so that core domain is available to the users of this run domain module
}