package com.techcode.gymcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.Room
import com.techcode.gymcontrol.data.db.AppDatabase
import com.techcode.gymcontrol.presentation.navegation.NavigationHost
import com.techcode.gymcontrol.presentation.theme.GymTheme
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel
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
