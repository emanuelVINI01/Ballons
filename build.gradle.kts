import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.emanuelvini"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:1.5.25")
    implementation("com.github.SaiintBrisson.command-framework:bukkit:1.3.1")
    implementation("com.github.henrysaantos.configuration-injector:bukkit:1.0.2")
    implementation("de.tr7zw:item-nbt-api:2.11.3")
    compileOnly("com.willfp:EcoEnchants:10.39.1")
    compileOnly(files("apis/AdvancedEnchantments-8.7.4.jar"))
    compileOnly(files("apis/EliteAPI-1.0-SNAPSHOT.jar"))

}

tasks.test {
    useJUnitPlatform()
}



tasks.named<ShadowJar>("shadowJar") {
    relocate("de.tr7zw.changeme.nbtapi", "com.emanuelvini.balloons.nbtapi")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}