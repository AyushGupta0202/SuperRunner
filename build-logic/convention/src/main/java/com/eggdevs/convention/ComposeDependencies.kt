package com.eggdevs.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addUiLayerDependencies(project: Project) {
    "implementation"(project(":core:presentation:designsystem"))
    "implementation"(project(":core:presentation:ui"))
    "implementation"(project.libs.findBundle("compose").get())
    "implementation"(project.libs.findBundle("koin.compose").get())
    "debugImplementation"(project.libs.findBundle("compose.debug").get())
}