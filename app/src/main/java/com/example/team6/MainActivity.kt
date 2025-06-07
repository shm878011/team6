package com.example.team6

import ClickTest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.team6.model.Click
import com.example.team6.navGraph.NavGraph
import com.example.team6.network.GeocodingTestScreen
import com.example.team6.ui.theme.Team6Theme
import com.example.team6.uicomponents.HomeScreen
import com.example.team6.uicomponents.NaverMapScreen
import com.example.team6.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Team6Theme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        //ClickTest()
                        NavGraph(navController = navController)
                        //HomeScreen()
                        //GeocodingTestScreen()
                    }
                }

            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Team6Theme {
//        Greeting("Android")
//    }
//}