package com.techcode.gymcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.techcode.gymcontrol.data.db.AppDatabase
import com.techcode.gymcontrol.presentation.navegation.NavigationHost
import com.techcode.gymcontrol.presentation.theme.GymTheme
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			GymTheme {
				val database = Room.databaseBuilder(this, AppDatabase::class.java, "db_usuarios").build()
				val dao = database.usuariosDao()
				NavigationHost(viewModel = PeopleViewModel(dao))
			}
		}
	}
}
