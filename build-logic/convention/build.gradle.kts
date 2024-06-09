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
        register("androidLibrary") { // register the AndroidLibraryConventionPlugin to be used in the project
            id = "superrunner.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") { // register the AndroidLibraryConventionPlugin to be used in the project
            id = "superrunner.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi") { // register the AndroidLibraryConventionPlugin to be used in the project
            id = "superrunner.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") { // register the AndroidLibraryConventionPlugin to be used in the project
            id = "superrunner.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}