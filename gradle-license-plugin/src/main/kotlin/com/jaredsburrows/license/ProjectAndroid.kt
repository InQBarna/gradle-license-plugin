package com.jaredsburrows.license

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import java.util.Locale

internal fun Project.onAndroidProject(action: (Project) -> Unit) {
  pluginManager.withPlugin("com.android.application") { action(this) }
  pluginManager.withPlugin("com.android.library") { action(this) }
  pluginManager.withPlugin("com.android.test") { action(this) }
}

/**
 * Configure for Android projects using the AGP 9+ variant API.
 *
 * Supports:
 *  - AppPlugin ("com.android.application")
 *  - LibraryPlugin ("com.android.library")
 *  - TestPlugin ("com.android.test")
 */
internal fun Project.configureAndroidProject() {
  val androidComponents = extensions.findByType(AndroidComponentsExtension::class.java) ?: return
  val commonExtension = extensions.findByType(CommonExtension::class.java) ?: return

  androidComponents.onVariants { variant ->

    logger.lifecycle("Configuring Android License Report for variant: ${variant.name}")

    val name =
      variant.name.replaceFirstChar {
        if (it.isLowerCase()) {
          it.titlecase(Locale.getDefault())
        } else {
          it.toString()
        }
      }

    tasks.register("license${name}Report", LicenseReportTask::class.java) {
      // Apply common task configuration first
      configureCommon(
        it,
        listOf(
          "${variant.name}CompileClasspath",
          "${variant.name}RuntimeClasspath",
        ),
      )

      // Custom for Android tasks
      val sourceSetName = if (it.useVariantSpecificAssetDirs) variant.name else "main"
      it.assetDirs = commonExtension.sourceSets
        .findByName(sourceSetName)
        ?.assets
        ?.directories
        ?.map { path -> file(path) }
        ?: emptyList()
      it.variantName = variant.name
    }
  }
}
