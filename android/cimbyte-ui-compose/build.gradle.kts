import com.android.build.gradle.LibraryExtension
import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

val libraryVersion = providers.gradleProperty("CIMBYTE_VERSION")
    .orElse(providers.environmentVariable("VERSION"))
    .getOrElse("0.2.1-SNAPSHOT")
val jitpackGroup = if (providers.environmentVariable("JITPACK").orNull == "true") {
    providers.environmentVariable("GROUP").orNull
        ?.let { group -> providers.environmentVariable("ARTIFACT").orNull?.let { artifact -> "$group.$artifact" } }
} else {
    null
}

group = jitpackGroup ?: "ro.cimbyte.ui"
version = libraryVersion

extensions.configure<LibraryExtension> {
    namespace = "ro.cimbyte.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(platform("androidx.compose:compose-bom:2024.10.01"))
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.ui:ui")
    api("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = project.group.toString()
                artifactId = "cimbyte-ui-compose"
                version = libraryVersion
                pom {
                    name.set("Cimbyte UI Compose")
                    description.set("Native Jetpack Compose components and design tokens for Cimbyte products.")
                    url.set("https://github.com/dragoscimpean/cimbyte-ui-kit")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/dragoscimpean/cimbyte-ui-kit.git")
                        developerConnection.set("scm:git:ssh://git@github.com/dragoscimpean/cimbyte-ui-kit.git")
                        url.set("https://github.com/dragoscimpean/cimbyte-ui-kit")
                    }
                }
            }
        }
    }
}
