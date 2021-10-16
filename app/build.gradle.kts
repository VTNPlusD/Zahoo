import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val configFilePath = "./src/main/assets/config.json"
val configFileEmptyMessage =
    "You are building Sinch Android Reference Sample Application project that requires presence of a configuration file with your auhthorization data.\n" +
        "Create and place your config.json file inside the " +
        "assets folder (app/src/main/assets) and re-build the application\n" +
        "Example of config.json:\n" +
        "[{\n" +
        "  \"name\": \"<ENVIRONMENT NAME\",\n" +
        "  \"appKey\": \"<YOUR SINCH APP KEY>\",\n" +
        "  \"appSecret\": \"<YOUR SINCH APP SECRET>\",\n" +
        "  \"environment\": \"ocra.api.sinch.com\"\n" +
        "}]\n" +
        "Check the \"Create configuration file\" section of the Readme file for more details regarding its content and structure."

val requiredConfigFileParamsMissingMessage =
    "You are building Sinch Android Reference Sample Application project that requires your application specific authorization data (key and secret).\n" +
        "Check config.json file located in app/src/main/assets folder and fill it with these values copied from Sinch portal website of your application.\n" +
        "Check the \"Create configuration file\" section of the Readme for more details regarding content and structure of the config.json file."

val googleServicesJsonFilePath = "google-services.json"
val googleServicesJsonMissingMessage =
    "You are building Sinch Android Reference Sample Application project that uses Firebase Cloud Messaging for delivering push notifications.\n" +
        "In order for push notification to work you must generate and include your application specific (with correspondent \"package_name\") google-services.json file.\n" +
        "Follow the Firebase Cloud Messaging manual here https://firebase.google.com/docs/android/setup to get one.\n" +
        "For more information regarding Sinch Managed Push Notifications refer to https://developers.sinch.com/docs/voice-android-cloud-push-notifications#2-provision-the-application-with-the-support-code"

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

task("runMakeSyncIfNeeded") {
    val libs = fileTree("libs").filter { it.isFile }.files.map { it.name }
    val isRTCSDKAlreadySynced = libs.find { it.contains("sinch-android-rtc") }
    isRTCSDKAlreadySynced?.let {
        finalizedBy("runMakeSync")
    } ?: run {
        println("Sync skipped. VVC SDK already present.")
    }
}

task<Exec>("runMakeSync") {
    workingDir("..")
    commandLine("make", "sync")
}

tasks {
    getByPath(":app:preBuild").dependsOn("ktlintFormat")
    getByPath(":app:preBuild").dependsOn("ktlintCheck")
    getByPath(":app:preBuild").dependsOn("detekt")
    getByPath(":app:preBuild").dependsOn("runMakeSyncIfNeeded")
    getByPath(":app:preBuild").doFirst {
        if (!file("./$googleServicesJsonFilePath").exists()) {
            ant.withGroovyBuilder {
                "echo"("level" to "error", "message" to googleServicesJsonMissingMessage)
            }
        }
    }
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(Deps.kotlin_stdlib)
    implementation(Deps.support_core_ktx)
    implementation(Deps.support_app_compat)
    implementation(Deps.support_design)
    implementation(Deps.support_constraintLayout)
    implementation(Deps.support_browser)

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
