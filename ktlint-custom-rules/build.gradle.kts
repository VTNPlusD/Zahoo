plugins {
    id(Plugins.kotlin)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Deps.kotlin_stdlib)
    compileOnly(Deps.ktlintCore)
}
