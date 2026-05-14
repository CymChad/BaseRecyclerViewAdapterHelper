import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    id("com.android.library")
    `maven-publish`
    signing
}

val versionName = "4.4.0"


android {
    namespace = "com.chad.library.adapter4"

    compileSdk = 35

    defaultConfig {
        minSdk = 19

        consumerProguardFiles("proguard-rules.pro")
    }


    buildTypes {
        getByName("release") {
            consumerProguardFiles("proguard-rules.pro")
        }
    }


    publishing {
        singleVariant("release") {
            // if you don't want sources/javadoc, remove these lines
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.addAll(
            "-module-name",
            "com.github.CymChad.brvah",
            "-Xjvm-default=all"
        )
    }
}


dependencies {
    implementation(libs.androidx.annotation)

    implementation(libs.androidx.recyclerview)

    compileOnly(libs.androidx.databinding.runtime)
}

//---------- maven upload info -----------------------------------

var signingKeyId = ""//签名的密钥后8位
var signingPassword = ""//签名设置的密码
var secretKeyRingFile = ""//生成的secring.gpg文件目录
var ossrhUsername = ""//sonatype用户名
var ossrhPassword = "" //sonatype密码


try {
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
} catch (_: Exception) {
}


afterEvaluate {

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "io.github.cymchad"
                artifactId = "BaseRecyclerViewAdapterHelper4"
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

//            maven {
//                setUrl("$rootDir/Repo")
//            }
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

