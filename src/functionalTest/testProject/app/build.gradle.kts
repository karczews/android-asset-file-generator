import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("com.github.utilx.android-assets-journalist")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.github.utilx.testapp"
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions("testDim")
    productFlavors {
        create("foo") { }
        create("bar") { }
    }
}

androidAssetsJournalist {
    xmlFile {
        enabled = true
        stringNamePrefix = "prefix_"
    }

    javaFile {
        enabled = true
        className = "AssetFilesJava"
        packageName = "com.github.utilx"
        constNamePrefix = "constprefixjava_"
        constValuePrefix = "javavalpref_"
        replaceInAssetsPath = listOf(
            listOf("match" to "^az", "replaceWith" to "replaceJava").toMap(),
            listOf("match" to "d[abc]", "replaceWith" to "java").toMap()
        )
    }

    kotlinFile {
        enabled = true
        className = "AssetFilesKotlin"
        packageName = "com.github.utilx"
        constNamePrefix = "constprefixkt_"
        constValuePrefix = "kotlinvalpref_"
        replaceInAssetsPath = listOf(
            listOf("match" to "^az", "replaceWith" to "replacekt").toMap(),
            listOf("match" to "d[abc]", "replaceWith" to "kt").toMap()
        )
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
}
