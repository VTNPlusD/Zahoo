import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.kotlinAndroid)
    kotlin(Plugins.kotlinApt)
    id(Plugins.navigationSafeArgs)
    id(Plugins.kotlinParcelize)
    id(Plugins.detekt).version(Versions.detekt)
    id(Plugins.googleServices)
    id(Plugins.firebaseCrashlytics)
}

buildscript {
    apply(from = "../autodimension.gradle.kts")
    apply(from = "../buildSrc/ktlint.gradle.kts")
}

tasks {
    getByPath(":app:preBuild").dependsOn("ktlintCheck")
    getByPath(":app:preBuild").dependsOn("detekt")
    // getByPath(":app:preBuild").dependsOn("ktlintFormat")
}

android {
    compileSdkVersion(Versions.compile_sdk_version)
    buildToolsVersion(Versions.build_tools_version)
    defaultConfig {
        applicationId = "com.duynn.zahoo"
        minSdkVersion(Versions.min_sdk_version)
        targetSdkVersion(Versions.target_sdk_version)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(
            file("proguard-rules.pro")
        )
        resValue("string", "admob_app_id", "ca-app-pub-2250523299023326~7343713608")
    }

    flavorDimensions("appVariant")

    productFlavors {
        create("dev") {
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "Zahoo Dev")
            buildConfigField("String", "BASE_URL", "\"https://google.com/\"")
        }
        create("prd") {
            resValue("string", "app_name", "Zahoo")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://google.com/\""
            )
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystores/android-keystores")
            storePassword = "Aa@123456"
            keyAlias = "upload"
            keyPassword = "Aa@123456"
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            versionNameSuffix = "-Debug"
        }
        getByName("release") {
            isDebuggable = false
            isZipAlignEnabled = true
            isShrinkResources = true
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            setProguardFiles(
                setOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    kapt {
        useBuildCache = true
    }

    configurations.all {
        resolutionStrategy.force("com.google.code.findbugs:jsr305:1.3.9")
    }

    lintOptions {
        disable("MissingTranslation")
        checkOnly("Interoperability")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.kotlin_stdlib)
    implementation(Deps.support_core_ktx)
    implementation(Deps.support_app_compat)
    implementation(Deps.support_design)
    implementation(Deps.support_constraintLayout)

    // Test
    implementation(Deps.mockito_inline)
    implementation(Deps.mockito_kotlin)
    implementation(Deps.arch_core_testing)
    implementation(Deps.junit)
    androidTestImplementation(Deps.atsl_runner)
    androidTestImplementation(Deps.atsl_ext_junit)
    androidTestImplementation(Deps.espresso_core)

    // Firebase
    implementation(Deps.firebase_database)
    implementation(Deps.firebase_storage)
    implementation(Deps.firebase_auth)
    implementation(Deps.firebase_messaging)
    implementation(Deps.firebase_analytics)
    implementation(Deps.firebase_crashlytics)

    // play service
    implementation(Deps.play_services_places)
    implementation(Deps.play_services_ads)

    // Timber
    implementation(Deps.timber)

    //Coroutine
    implementation(Deps.coroutines_core)
    implementation(Deps.coroutines_android)
    implementation(Deps.coroutines_play_services)
    implementation(Deps.support_core_ktx)
    testImplementation(Deps.coroutines_test)

    //Koin
    implementation(Deps.koin_ext)
    implementation(Deps.koin_android)
    implementation(Deps.koin_android)
    implementation(Deps.koin_viewmodel)
    implementation(Deps.koin_ext)
    kapt(Deps.support_databinding)

    //Json
    implementation(Deps.retrofit_runtime)
    implementation(Deps.converterMoshi)
    implementation(Deps.okhttp_logging_interceptor)
    implementation(Deps.okhttp)
    implementation(Deps.moshi)
    implementation(Deps.moshiAdapter)
    implementation(Deps.moshiKotlin)
    kapt(Deps.moshiCodegen)

    // preference
    implementation(Deps.preference)

    //Lifecycler
    implementation(Deps.lifecycle_viewmodel_ktx)
    implementation(Deps.lifecycle_extension)
    implementation(Deps.lifecycle_runtime)
    implementation(Deps.lifecycle_livedata_ktx)
    implementation(Deps.lifecycle_java8)
    kapt(Deps.lifecycle_compiler)

    //Room
    implementation(Deps.room_runtime)
    implementation(Deps.room_testing)
    implementation(Deps.room_ktx)
    kapt(Deps.room_compiler)

    //Navigation
    implementation(Deps.navigation_fragment)
    implementation(Deps.navigation_ui)
    implementation(Deps.navigation_fragment_ktx)
    implementation(Deps.navigation_ui_ktx)

    //otp
    implementation(Deps.otp_view)

    // androidx preference
    implementation(Deps.preference)
    implementation(Deps.legacy_preference)

    // more
    implementation(Deps.swiperefreshlayout)
    implementation(Deps.support_recyclerview)


    /*
    implementation(Deps.support_annotations)
    implementation(Deps.support_cardview)
    implementation(Deps.dexter) //Permissionx
    // gilde
    implementation(Deps.glide_runtime)
    kapt(Deps.glide_compiler)

    //Circle ImageView
    implementation(Deps.circle_image)
    */
}
