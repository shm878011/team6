package com.example.team6.uicomponents

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.team6.model.BottomNavRoute

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar(
        containerColor = Color(0xeaeaea),
        modifier = Modifier.height(94.dp),
        tonalElevation = 8.dp
    ) {
        BottomNavRoute.items.forEach { screen ->
            val selected = currentDestination?.route == screen.route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        tint = if (selected) Color.Black else Color.Gray
                    )
                },
                label = { Text(screen.label) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(screen.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }
}