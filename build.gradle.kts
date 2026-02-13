import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intelliJPlatform)
    alias(libs.plugins.changelog)
    alias(libs.plugins.composeCompiler)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin {
    jvmToolchain(21)

    compilerOptions {
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
            )
        )
    }
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
    google()
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)
    testImplementation(libs.hamcrest)
    testImplementation(libs.composeuitest)
    testImplementation(libs.jewelstandalone)
    testImplementation(libs.skikoAwtRuntimeAll)

    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

        @Suppress("UnstableApiUsage")
        composeUI()

        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
        bundledModules(providers.gradleProperty("platformBundledModules").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    //signing {
    //    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    //    privateKey = providers.environmentVariable("PRIVATE_KEY")
    //    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    //}

    //publishing {
    //    token = providers.environmentVariable("PUBLISH_TOKEN")
    //    // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
    //    // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
    //    // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
    //    channels = providers.gradleProperty("pluginVersion")
    //        .map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    //}

    pluginVerification {
        ides {
            create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
        }
    }
}

changelog {
    version = providers.gradleProperty("pluginVersion")
    path = "${project.rootDir}/CHANGELOG.md"
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }
}

// do not run Plugin Verifier in the template itself
//tasks.getByName("verifyPlugin").enabled = false
