plugins {
    `kotlin-dsl`
}

group = "com.eggdevs.superrunner.buildlogic"

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
        register("androidApplicationWearCompose") { // register the AndroidApplicationWearComposeConventionPlugin to be used in the project
            id = "superrunner.android.application.wear.compose"
            implementationClass = "AndroidApplicationWearComposeConventionPlugin"
        }
        register("androidLibrary") { // register the AndroidLibraryConventionPlugin to be used in the project
            id = "superrunner.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") { // register the AndroidLibraryComposeConventionPlugin to be used in the project
            id = "superrunner.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi") { // register the AndroidFeatureUiConventionPlugin to be used in the project
            id = "superrunner.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") { // register the AndroidRoomConventionPlugin to be used in the project
            id = "superrunner.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidDynamicFeature") { // register the AndroidDynamicFeatureConventionPlugin to be used in the project
            id = "superrunner.android.dynamic.feature"
            implementationClass = "AndroidDynamicFeatureConventionPlugin"
        }
        register("jvmLibrary") { // register the JvmLibraryConventionPlugin to be used in the project
            id = "superrunner.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmKtor") { // register the JvmKtorConventionPlugin to be used in the project
            id = "superrunner.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
        register("jvmJunit5") { // register the JvmJUnit5ConventionPlugin to be used in the project
            id = "superrunner.jvm.junit5"
            implementationClass = "JvmJUnit5ConventionPlugin"
        }
        register("androidJunit5") { // register the AndroidJUnit5ConventionPlugin to be used in the project
            id = "superrunner.android.junit5"
            implementationClass = "AndroidJUnit5ConventionPlugin"
        }
    }
}