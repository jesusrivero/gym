package com.techcode.gymcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.presentation.ui.AppNavegation

import com.techcode.gymcontrol.presentation.ui.componentes.BottomNavigationBar
import com.techcode.gymcontrol.ui.theme.GymTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			GymTheme {
				val navController = rememberNavController()
				Scaffold(
					bottomBar = { BottomNavigationBar(
						navController = navController,
						modifier = Modifier.fillMaxWidth()
					) }
				) { innerPadding ->
					
					AppNavegation(navController = navController,
						modifier = Modifier.padding(innerPadding))
					
				}
			}
		}
	}
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	GymTheme {
		AppNavegation(navController = rememberNavController(), modifier = Modifier)
	}
}

