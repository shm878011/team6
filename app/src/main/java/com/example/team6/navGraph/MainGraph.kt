package com.example.team6.navGraph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.team6.uicomponents.BottomNavigationBar
import com.example.team6.model.BottomNavRoute
import com.example.team6.model.Nursery
import com.example.team6.model.Routes
import com.example.team6.model.UserInfo
import com.example.team6.uicomponents.AccountInfoScreen
import com.example.team6.uicomponents.FavoriteNurseriesScreen
import com.example.team6.uicomponents.LocationSettingScreen
import com.example.team6.uicomponents.MyPageScreen
import com.example.team6.uicomponents.MyReviewsScreen
import com.example.team6.uicomponents.MapScreen
import com.example.team6.uicomponents.InfoScreen
import com.example.team6.uicomponents.recommend.QuestionScreen
import com.example.team6.uicomponents.recommend.RecommendScreen
import com.example.team6.uicomponents.recommend.ResultScreen
import com.example.team6.viewmodel.MainViewModel



@Composable
fun MainScreen(
    onLogout: () -> Unit // 상위 NavController에서 전달
){
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    //val likedNurseries = remember { mutableStateListOf<Nursery>() }


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
                MapScreen(viewModel)
            }
            composable(BottomNavRoute.Search.route) {
                RecommendScreen(navController)
            }
            composable(BottomNavRoute.Info.route) {
                InfoScreen()
            }

            composable(BottomNavRoute.MyPage.route) {
                MyPageScreen(navController)
            }
            composable("account_info") {
                AccountInfoScreen(
                    navController = navController,
                    userInfo = UserInfo(
                        name = "홍길동",
                        username = "konkuk",
                        email = "konkuk@gmail.com",
                        passwordMasked = "************"
                    ),
                    onLogout = onLogout

                )
            }


            composable("my_reviews") {
                MyReviewsScreen(navController)
            }
            composable("favorite_nurseries") {
                FavoriteNurseriesScreen(favorites = viewModel.likedNurseries, navController)
            }
            composable("location_setting") {
                LocationSettingScreen(navController)
            }

            composable("question") {
                QuestionScreen(navController = navController)
            }
            composable("result") {
                ResultScreen(navController = navController)
            }
        }
    }
}
