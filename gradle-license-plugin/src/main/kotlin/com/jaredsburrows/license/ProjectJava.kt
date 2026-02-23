package com.jaredsburrows.license

import org.gradle.api.Project

internal fun Project.onJavaProject(action: (Project) -> Unit) {
  pluginManager.withPlugin("java") { action(this) }
  pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { action(this) }
}

/** Configure for Java projects. */
internal fun Project.configureJavaProject() {
  tasks.register("licenseReport", LicenseReportTask::class.java) {
    // Apply common task configuration first
    configureCommon(it, listOf("compileClasspath", "runtimeClasspath"))
  }
}
