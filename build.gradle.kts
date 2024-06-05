import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.versionCheck)
    alias(libs.plugins.androidApplication) apply false
}

subprojects {
    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
        plugin(rootProject.libs.plugins.kotlinter.get().pluginId)
    }

    detekt {
        config.from(rootProject.files("config/detekt/detekt.yml"))
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}

fun String.isNonStable() = "^[0-9,.v-]+(-r)?$".toRegex().matches(this).not()

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("reformatAll") {
    description = "Reformat all the Kotlin Code"

    dependsOn("ktlintFormat")
    dependsOn(gradle.includedBuild("plugin-build").task(":plugin:ktlintFormat"))
}

tasks.register("preMerge") {
    description = "Runs all the tests/verification tasks on both top level and included build."

    dependsOn(":example:check")
    dependsOn(gradle.includedBuild("plugin-build").task(":plugin:check"))
    dependsOn(gradle.includedBuild("plugin-build").task(":plugin:validatePlugins"))
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
