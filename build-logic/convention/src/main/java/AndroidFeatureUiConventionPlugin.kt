import com.eggdevs.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeatureUiConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("superrunner.android.library.compose")
            }
            dependencies {
                addUiLayerDependencies(target)
                "implementation"(project(":core:utils"))
            }
        }
    }
}