package com.example.team6.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.team6.model.Routes
import com.example.team6.uicomponents.FavoriteNurseriesScreen
import com.example.team6.uicomponents.LoginScreen
import com.example.team6.uicomponents.SignupScreen
import com.example.team6.viewmodel.MainViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToSignup = { navController.navigate(Routes.Signup.route) },
                onGuestLogin = { navController.navigate(Routes.Home.route) },
                onSuccessLogin = { navController.navigate(Routes.Home.route) }
            )
        }

        composable(Routes.Signup.route) {
            SignupScreen(
                onBackToLogin = {
                    navController.popBackStack(Routes.Login.route, inclusive = false)
                }
            )
        }

        composable(Routes.Home.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}
