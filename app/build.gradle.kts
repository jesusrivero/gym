

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlinx.serialization.json)
	alias(libs.plugins.ksp)
	alias(libs.plugins.hilt.android)
	kotlin("kapt")
	id("com.google.gms.google-services")
	
	
}

android {
	namespace = "com.jesus.gymcontrol"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.jesus.gymcontrol"
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
	implementation (libs.accompanist.pager)  // Alternativa si usas Material
	implementation(libs.kotlinx.serialization.json)              // JSON con Kotlinx
	implementation(libs.androidx.room.runtime)                   // Room runtime
	implementation(libs.androidx.room.ktx)
	implementation(libs.androidx.compiler)
	implementation(libs.androidx.datastore.core.android)                       // Room con corutinas
	ksp(libs.androidx.room.compiler)                             // Room compiler
	implementation(libs.androidx.lifecycle.viewmodel.compose)    // ViewModel + Compose
	implementation(libs.androidx.navigation.compose)             // Navigation en Compose
	implementation(libs.hilt.android)                            //Hilt Android
	implementation (libs.androidx.hilt.navigation.compose)       // Hilt navigation
	kapt(libs.hilt.compiler)                                     // Hilt compiler
	implementation(libs.coil.compose)                            //coil
	implementation (libs.firebase.messaging)
	implementation(libs.androidx.datastore.preferences)
	implementation (libs.androidx.material.icons.extended)
	implementation (libs.guava)
	implementation(platform(libs.firebase.bom))
	implementation (libs.google.firebase.analytics)
	implementation(libs.firebase.auth.ktx)
	implementation(libs.firebase.firestore.ktx)
	implementation(libs.kotlinx.coroutines.play.services)

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