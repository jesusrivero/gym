package com.jesus.gymcontrol.infraestructure

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.FirebaseApp
import com.jesus.gymcontrol.domain.viewmodels.AuthViewModel
import com.jesus.gymcontrol.presentation.navegation.NavigationHost
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.PendingOverlay
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		FirebaseApp.initializeApp(this)
		
		enableEdgeToEdge()
		setContent {
			val viewModel: AuthViewModel = hiltViewModel()
			var userState by remember { mutableStateOf<String?>(null) }
			
			LaunchedEffect(Unit) {
				viewModel.checkUserStatusOnStart { state ->
					userState = state
				}
			}
			
			GymTheme {
				Box(modifier = Modifier.fillMaxSize()) {
					NavigationHost()
					
					if (userState == "pendiente") {
						PendingOverlay()
					}
				}
			}
		}
	}
}

