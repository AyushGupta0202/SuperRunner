import com.android.build.api.dsl.DynamicFeatureExtension
import com.eggdevs.convention.ExtensionType
import com.eggdevs.convention.addUiLayerDependencies
import com.eggdevs.convention.configureAndroidCompose
import com.eggdevs.convention.configureBuildTypes
import com.eggdevs.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/* This plugin will be applied to the app module gradle file. */
class AndroidDynamicFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("superrunner.jvm.junit5")
                apply("superrunner.android.junit5")
            }

            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.DYNAMIC_FEATURE
                )
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}