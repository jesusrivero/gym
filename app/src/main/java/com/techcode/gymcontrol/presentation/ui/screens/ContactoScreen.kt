package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.people.PeopleViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    navController: NavController,
) {
    MainContent(
        navBottom = navController,


        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navBottom: NavController,

    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navBottom.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(id = com.techcode.gymcontrol.R.drawable.ic_back),
                            contentDescription = "Regresar", tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            )
        }
    )
    { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(text = "Contactos",fontSize = 50.sp)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Hola bebe",fontSize = 15.sp)
            Text(text = "Hola Mundo",fontSize = 15.sp)


        }

    }
}






	
