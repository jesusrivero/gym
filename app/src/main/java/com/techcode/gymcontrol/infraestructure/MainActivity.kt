package com.techcode.gymcontrol.infraestructure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techcode.gymcontrol.presentation.navegation.NavigationHost
import com.techcode.gymcontrol.presentation.theme.GymTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			GymTheme {
				NavigationHost()
			}
		}
	}
}