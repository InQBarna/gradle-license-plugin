package com.jaredsburrows.license

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.cc.base.logger

/** A [Plugin] which grabs the POM.xml files from maven dependencies. */
class LicensePlugin : Plugin<Project> {
  override fun apply(project: Project) {

    logger.lifecycle("Creating License Report Extension for project: ${project.name}")
    project.extensions.create("licenseReport", LicenseReportExtension::class.java)

    project.onJavaProject {
      project.logger.lifecycle("Applying Java License Report on: ${project.name}")
      it.configureJavaProject()
    }

    project.onAndroidProject {
      project.logger.lifecycle("Applying Android License Report on: ${project.name}")
      it.configureAndroidProject()
    }
  }
}
