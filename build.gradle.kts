plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

tasks.register("publishAll") {
    group = "publishing"
    description = "构建、签名并打包为手动上传 ZIP（仅本地，不上传）"
    dependsOn(":library:bundleMavenCentral")
}
