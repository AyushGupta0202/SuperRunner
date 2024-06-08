package com.eggdevs.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/** Access the libs.version from the project */
val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")