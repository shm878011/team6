package com.example.team6.navGraph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.team6.uicomponents.BottomNavigationBar
import com.example.team6.model.BottomNavRoute
import com.example.team6.model.Routes
import com.example.team6.screens.InfoMainScreen
import com.example.team6.uicomponents.AccountInfoScreen
import com.example.team6.uicomponents.FavoriteNurseriesScreen
import com.example.team6.uicomponents.LocationSettingScreen
import com.example.team6.uicomponents.MyPageScreen
import com.example.team6.uicomponents.MyReviewsScreen
import com.example.team6.uicomponents.MapScreen
import com.example.team6.uicomponents.info.EduDetailScreen
import com.example.team6.uicomponents.info.FoodDetailScreen
import com.example.team6.uicomponents.info.NapDetailScreen
import com.example.team6.uicomponents.info.PlayDetailScreen
import com.example.team6.uicomponents.recommend.QuestionScreen
import com.example.team6.uicomponents.recommend.RecommendScreen
import com.example.team6.uicomponents.recommend.ResultScreen
import com.example.team6.viewmodel.FirebaseAuthViewModel
import com.example.team6.viewmodel.MainViewModel



@Composable
fun MainScreen(
    onLogout: () -> Unit // 상위 NavController에서 전달
){
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val authViewModel: FirebaseAuthViewModel = viewModel()

    LaunchedEffect(Unit) {
        authViewModel.checkLoginStatus()
    }

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
                InfoMainScreen(navController)
            }

            composable("nap_detail")  { NapDetailScreen(navController) }
            composable("play_detail") { PlayDetailScreen(navController) }
            composable("food_detail") { FoodDetailScreen(navController) }
            composable("edu_detail") { EduDetailScreen(navController) }


            composable(BottomNavRoute.MyPage.route) {
                MyPageScreen(navController)
            }
            composable("account_info") {
                AccountInfoScreen(
                    navController = navController,
                    viewModel = authViewModel,
                    mainViewModel = viewModel,
                    onLogout = onLogout
                )
            }


            composable("my_reviews") {
                MyReviewsScreen(navController)
            }
            composable("favorite_nurseries") {
                FavoriteNurseriesScreen(favorites = viewModel.likedNurseries, navController, viewModel = viewModel)
            }
            composable("location_setting") {
                LocationSettingScreen(navController,viewModel)
            }

            composable("question") {
                QuestionScreen(navController = navController)
            }
            composable(
                route = Routes.ResultWithArgs.route,
                arguments = listOf(
                    navArgument("age") { type = NavType.StringType },
                    navArgument("importantPoint") { type = NavType.StringType },
                    navArgument("guardianAvailable") { type = NavType.StringType },
                    navArgument("active") { type = NavType.StringType } ,
                    navArgument("nowadmission") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // NavBackStackEntry에서 인자를 추출합니다.
                val age = backStackEntry.arguments?.getString("age") ?: ""
                val importantPoint = backStackEntry.arguments?.getString("importantPoint") ?: ""
                val guardianAvailable = backStackEntry.arguments?.getString("guardianAvailable") ?: ""
                val active = backStackEntry.arguments?.getString("active") ?: ""
                val nowadmission = backStackEntry.arguments?.getString("nowadmission") ?: ""

                ResultScreen(
                    navController = navController,
                    age = age,
                    importantPoint = importantPoint,
                    guardianAvailable = guardianAvailable,
                    active = active,
                    now = nowadmission,
                    viewModel = viewModel
                )
            }
        }
    }
}
