package com.jesus.gymcontrol.presentation.ui.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jesus.gymcontrol.presentation.navegation.AppRoutes
import com.jesus.gymcontrol.presentation.theme.GymTheme
import com.jesus.gymcontrol.presentation.ui.commons.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(navController: NavController) {
    GymTheme {
        PreferencesContent(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesContent(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel de administración",
                        color = colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSectionTitle("General")

            SettingsItem(
                text = "Cuenta y Datos",
                icon = Icons.Default.AccountCircle,
                onClick = { navController.navigate(AppRoutes.LoginScreen) }
            )

            SettingsItem(
                text = "Notificaciones",
                icon = Icons.Default.Notifications,
                onClick = { navController.navigate(AppRoutes.SelectedRolScreen) }
            )

            SettingsItem(
                text = "Seguridad",
                icon = Icons.Default.Build,
                onClick = { navController.navigate(AppRoutes.SecurityScreen) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionTitle("Soporte")

            SettingsItem(
                text = "Reportar errores",
                icon = Icons.Default.Email,
                onClick = { navController.navigate(AppRoutes.ErrorReportScreen) }
            )

            SettingsItem(
                text = "Sobre nosotros",
                icon = Icons.Default.Info,
                onClick = { navController.navigate(AppRoutes.ContactScreen) }
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navegar",
                    tint = colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Divider(
            color = colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(start = 56.dp, end = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesPreview() {
    GymTheme {
        PreferencesContent(
            navController = NavController(LocalContext.current)
        )
    }
}