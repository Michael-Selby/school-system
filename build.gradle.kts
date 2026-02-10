plugins {
    kotlin("jvm") version "1.9.0" apply false
    kotlin("android") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.android.application") version "8.2.0" apply false
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
