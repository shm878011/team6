package com.example.team6.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavRoute(val route: String, val label: String, val icon: ImageVector) {
    object Map : BottomNavRoute("map", "지도", Icons.Default.LocationOn)
    object Search : BottomNavRoute("search", "추천", Icons.Default.Face)
    object Info : BottomNavRoute("info", "정보", Icons.Default.Info)
    object MyPage : BottomNavRoute("mypage", "마이페이지", Icons.Default.Person)

    companion object {
        val items = listOf(Map, Search, Info, MyPage)
    }
}