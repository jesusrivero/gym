package com.techcode.gymcontrol.presentation.ui.settings.details

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.techcode.gymcontrol.R

@Composable
fun NotificationScreen(
    navController: NavController,
) {
    NotificationScreenContent(
        navBottom = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreenContent(
    navBottom: NavController,
) {
    var newClientEnabled by remember { mutableStateOf(true) }
    var paymentRegisteredEnabled by remember { mutableStateOf(true) }
    var membershipExpirationEnabled by remember { mutableStateOf(true) }
    var weekStartEnabled by remember { mutableStateOf(true) } // Nuevo estado para Inicio de semana
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
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
                            contentDescription = "Regresar",
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
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "Tipos de notificaciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )


            NotificationOptionItem(
                title = "Cliente nuevo",
                enabled = newClientEnabled,
                onCheckedChange = { newClientEnabled = it }
            )
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            NotificationOptionItem(
                title = "Pago registrado",
                enabled = paymentRegisteredEnabled,
                onCheckedChange = { paymentRegisteredEnabled = it }
            )
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            NotificationOptionItem(
                title = "Vencimiento de membresía",
                enabled = membershipExpirationEnabled,
                onCheckedChange = { membershipExpirationEnabled = it }
            )
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))


            NotificationOptionItem(
                title = "Inicio de semana",
                enabled = weekStartEnabled,
                onCheckedChange = { weekStartEnabled = it }
            )
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Canal de notificaciones",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            NotificationOptionItem(
                title = "Notificaciones push",
                enabled = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it }
            )
        }
    }
}

@Composable
fun NotificationOptionItem(
    title: String,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.width(4.dp))

        Switch(
            checked = enabled,
            onCheckedChange = onCheckedChange,
            thumbContent = if (enabled) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationContentPreview() {

        NotificationScreenContent(navBottom = rememberNavController())

    }