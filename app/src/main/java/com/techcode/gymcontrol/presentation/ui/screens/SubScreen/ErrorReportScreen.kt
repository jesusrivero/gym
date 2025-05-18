package com.techcode.gymcontrol.presentation.ui.screens.SubScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.techcode.gymcontrol.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorReportScreen(
    navController: NavController,
) {
    ErrorReportContent(
        navBottom = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorReportContent(
    navBottom: NavController,
) {
    var errorDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reportar Problema",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navBottom.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_payments),
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "¿Qué salió mal?",
                    style = MaterialTheme.typography.titleLarge
                )
            }


            TextField(
                value = errorDescription,
                onValueChange = { errorDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                label = { Text("Describe el problema") },
                placeholder = { Text("Ejemplo: La app se cierra cuando...") },
            )

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {

                    navBottom.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = errorDescription.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            ) {
                Text("Enviar Reporte")
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = "O contáctanos:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Color(0xBAA7D3DC)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "soporte@tuapp.com",
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Teléfono",
                    tint = Color(0xBAA7D3DC)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+58 000 0000",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
