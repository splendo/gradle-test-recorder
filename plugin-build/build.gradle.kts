import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.pluginPublish) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.versionCheck)
    alias(libs.plugins.kover)
}

allprojects {
    group = property("GROUP").toString()
    version = property("VERSION").toString()

    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
        plugin(rootProject.libs.plugins.kotlinter.get().pluginId)
    }

    detekt {
        config.from(rootProject.files("../config/detekt/detekt.yml"))
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

kover {

}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
