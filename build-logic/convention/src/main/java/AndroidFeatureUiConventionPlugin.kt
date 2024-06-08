import com.android.build.api.dsl.LibraryExtension
import com.eggdevs.convention.ExtensionType
import com.eggdevs.convention.addUiLayerDependencies
import com.eggdevs.convention.configureAndroidCompose
import com.eggdevs.convention.configureBuildTypes
import com.eggdevs.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureUiConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("superrunner.android.library.compose")
            }
            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}