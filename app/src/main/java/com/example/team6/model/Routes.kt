package com.example.team6.model

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Signup : Routes("signup")
    object Home : Routes("home")
}