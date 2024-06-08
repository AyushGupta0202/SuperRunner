plugins {
    `kotlin-dsl`
}

group = "com.eggdevs.runique.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") { // register the AndroidApplicationConventionPlugin to be used in the project
            id = "superrunner.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") { // register the AndroidApplicationComposeConventionPlugin to be used in the project
            id = "superrunner.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}