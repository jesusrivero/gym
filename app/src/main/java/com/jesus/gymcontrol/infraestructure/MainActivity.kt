package com.jesus.gymcontrol.infraestructure

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.jesus.gymcontrol.presentation.navegation.NavigationHost
import com.jesus.gymcontrol.presentation.theme.GymTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			GymTheme {
				NavigationHost()
				FirebaseApp.initializeApp(this)
			}
		}
	}
}