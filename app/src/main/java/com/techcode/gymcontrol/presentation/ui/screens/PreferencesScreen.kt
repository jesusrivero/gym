package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ManageScreen(navController: NavController) {
//	ManagerContent(

//		navBottom = navController,
//		navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },

//		)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(

    navController: NavController,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel de Administración",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            SettingsSectionTitle("General")

            SettingsItem(
                text = "Información de la cuenta",
                icon = Icons.Default.AccountCircle,
                onClick = { navController.navigate(AppRoutes.MainScreen) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))



            SettingsItem(
                text = "Notificaciones",
                icon = Icons.Default.Notifications,
                onClick = { navController.navigate("") }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

	        

            SettingsItem(
                text = "Bitácora de acciones",
                icon = Icons.Default.Build,
                onClick = { navController.navigate("") }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            Spacer(modifier = Modifier.padding(top = 15.dp))

            SettingsSectionTitle("Soporte")

            SettingsItem(
                text = "Reportar errores",
                icon = Icons.Default.Email,
                onClick = { navController.navigate(AppRoutes.ErrorReportScreen) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

            SettingsItem(
                text = "Sobre nosotros",
                icon = Icons.Default.Info,
                onClick = { navController.navigate(AppRoutes.ContactScreen) }
            )

            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}


@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .padding(start = 10.dp, top = 15.dp)
    )
}

@Composable
fun SettingsItem(
    text: String,
    disabledTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector,
    onClick: () -> Unit
) {
    MaterialTheme {
        TextButton(modifier = Modifier.fillMaxWidth()
            ,onClick = onClick
            ,colors = ButtonDefaults.textButtonColors(
                contentColor = textColor,
                disabledContentColor = disabledTextColor
            )) {

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 5.dp)
                    .padding(end = 5.dp)
                    ,verticalAlignment = Alignment.CenterVertically
                    ,horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.padding(start = 8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )


                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navegar",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }


        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
	ManageScreen(
		navBottom = NavController(LocalContext.current),
		
	)
}*/