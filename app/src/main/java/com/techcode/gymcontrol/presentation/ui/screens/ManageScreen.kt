package com.techcode.gymcontrol.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.techcode.gymcontrol.presentation.navegation.AppRoutes
import com.techcode.gymcontrol.presentation.ui.commons.BottomNavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(navController: NavController) {
    ManagerContent(
        navBottom = navController,
        navRegister = { navController.navigate(AppRoutes.RegPersonScreen) },

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerContent(

    navBottom: NavController,
    navRegister: () -> Unit,

    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Panel de administracion", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xBAA7D3DC)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navBottom,
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Column (modifier=Modifier.fillMaxSize()
                ){



                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        start = Offset(0f, Float.POSITIVE_INFINITY),
                                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                                    )
                                )
                        ) {
                            Image(
                                painter = painterResource(id =com.techcode.gymcontrol.R.drawable.image),
                                contentDescription = "Fondo de la tarjeta",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Text(
                                "Contenido sobre la imagen",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
                            )
                        }

                }


                }


            }




        }
    }


@Preview(showBackground = true)
@Composable
fun ManagerScreenPreview() {
    ManagerContent(
        navBottom = NavController(LocalContext.current),
        navRegister = {}
    )
}


