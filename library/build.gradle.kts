import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
    signing
}

val versionName = "4.0.0-alpha4"


android {
    compileSdk = 30

    defaultConfig {
        minSdk = 16
        targetSdk = 30
    }


    buildTypes {
        getByName("release") {
            consumerProguardFiles("proguard-rules.pro")
        }
    }


    compileOptions {
        kotlinOptions.freeCompilerArgs = ArrayList<String>().apply {
            add("-module-name")
            add("com.github.CymChad.brvah")
            add("-Xjvm-default=all")
        }
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


    buildFeatures {
        viewBinding = true
        dataBinding = true
    }


    publishing {
        singleVariant("release") {
            // if you don't want sources/javadoc, remove these lines
            withSourcesJar()
            withJavadocJar()
        }
    }
}


dependencies {
    implementation("androidx.annotation:annotation:1.3.0")

    implementation("androidx.recyclerview:recyclerview:1.3.0-beta02")
}


var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录
var ossrhUsername = ""//sonatype用户名
var ossrhPassword = "" //sonatype密码


val localProperties: File = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signing.keyId")
    signingPassword = properties.getProperty("signing.password")
    secretKeyRingFile = properties.getProperty("signing.secretKeyRingFile")
    ossrhUsername = properties.getProperty("ossrhUsername")
    ossrhPassword = properties.getProperty("ossrhPassword")

} else {
    println("No props file, loading env vars")
}

afterEvaluate {

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "io.github.cymchad"
                artifactId = "BaseRecyclerViewAdapterHelper"
                version = versionName

                pom {
                    name.value("BaseRecyclerViewAdapterHelper")
                    description.value("Powerful and flexible RecyclerAdapter")
                    url.value("https://github.com/CymChad/BaseRecyclerViewAdapterHelper")

                    licenses {
                        license {
                            //协议类型
                            name.value("The MIT License")
                            url.value("https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.value("limuyang2")
                            name.value("limuyang")
                            email.value("limuyang2@hotmail.com")
                        }
                    }

                    scm {
                        connection.value("scm:git@github.com/CymChad/BaseRecyclerViewAdapterHelper.git")
                        developerConnection.value("scm:git@github.com/CymChad/BaseRecyclerViewAdapterHelper.git")
                        url.value("https://github.com/CymChad/BaseRecyclerViewAdapterHelper")
                    }
                }
            }

        }

        repositories {
            maven {
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }

    }

}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {

        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}

