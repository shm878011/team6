package com.example.team6.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.team6.viewmodel.FirebaseAuthViewModel

@Composable
fun MyPageScreen(navController: NavController,
                 viewModel: FirebaseAuthViewModel = viewModel()
) {
    val nickname by viewModel.nickname.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNicknameFromDatabase()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("마이페이지", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(24.dp))

        // 🐻 곰돌이 이미지 (예: R.drawable.bear_face)
        // 프로필 이미지 영역
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "기본 프로필",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            tint = Color.Gray
        )


        Spacer(modifier = Modifier.height(12.dp))

        Text(nickname.ifBlank { "사용자" }, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("${nickname}님, 오늘도 반가워요!", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        // 메뉴 항목들 (단순 리스트 형태)
        MenuTextItem("계정 정보") { navController.navigate("account_info") }
        MenuTextItem("내 리뷰 보기") { navController.navigate("my_reviews") }
        MenuTextItem("관심 어린이집 보기") { navController.navigate("favorite_nurseries") }
        MenuTextItem("위치 설정") { navController.navigate("location_setting") }
    }
}

@Composable
fun MenuTextItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 16.sp, color = Color.Black)
        Text(">", fontSize = 16.sp, color = Color.Gray)
    }
}



// 내부에서 사용할 Preview
@Preview(showBackground = true)
@Composable
fun MyPageWithBottomNavPreview() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyPageScreen(navController = navController)
        }
    }
}
