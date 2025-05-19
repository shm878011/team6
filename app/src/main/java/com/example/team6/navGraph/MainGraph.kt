package com.example.team6.navGraph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.team6.BottomNavigationBar
import com.example.team6.model.BottomNavRoute
import com.example.team6.uicomponents.InfoScreen
import com.example.team6.uicomponents.MapScreen
import com.example.team6.uicomponents.MyPageScreen
import com.example.team6.uicomponents.RecommendScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavRoute.Map.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavRoute.Map.route) {
                MapScreen()
            }
            composable(BottomNavRoute.Search.route) {
                RecommendScreen()
            }
            composable(BottomNavRoute.Info.route) {
                InfoScreen()
            }
            composable(BottomNavRoute.MyPage.route) {
                MyPageScreen()
            }
        }
    }
}