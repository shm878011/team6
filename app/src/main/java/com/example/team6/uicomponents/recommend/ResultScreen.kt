import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.team6.model.KindergartenInfo

@Composable
fun ResultScreen(navController: NavController) {

    val sampleList = listOf(
        KindergartenInfo("자양어린이집", 4.8, "860m", "서울특별시 광진구 자양번영로 35 자양3세대복합센터"),
        KindergartenInfo("자양어린이집", 4.8, "860m", "서울특별시 광진구 자양번영로 35 자양3세대복합센터"),
        KindergartenInfo("자양어린이집", 4.8, "860m", "서울특별시 광진구 자양번영로 35 자양3세대복합센터")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "어린이집 추천 결과",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            items(sampleList) { item ->
                KindergartenCard(item)
            }
        }
    }
}

@Composable
fun KindergartenCard(info: KindergartenInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F8F8)
        )

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = info.name, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(18.dp)
                )
                Text(text = "${info.rating}", fontSize = 14.sp)
                Text(text = "   거리 ${info.distance}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = info.address, fontSize = 13.sp)
        }
    }
}