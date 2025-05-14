plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlinx.serialization.json)
	alias(libs.plugins.ksp)


}

android {
	namespace = "com.techcode.gymcontrol"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.techcode.gymcontrol"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		//noinspection WrongGradleMethod
		ksp {
			arg("room.schemaLocation", "${projectDir}/schemas")
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
}

dependencies {
	
	implementation (libs.androidx.foundation)
	implementation (libs.accompanist.pager)  // Alternativa si usas Material 2
	implementation(libs.kotlinx.serialization.json)              // JSON con Kotlinx
	implementation(libs.androidx.room.runtime)                   // Room runtime
	implementation(libs.androidx.room.ktx)                       // Room con corutinas
	ksp(libs.androidx.room.compiler)                             // Room compiler
	implementation(libs.androidx.lifecycle.viewmodel.compose)    // ViewModel + Compose
	implementation(libs.androidx.navigation.compose)             // Navigation en Compose

	implementation(libs.hilt.android)
	implementation (libs.androidx.hilt.navigation.compose)
	//DEFAULT
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)
}