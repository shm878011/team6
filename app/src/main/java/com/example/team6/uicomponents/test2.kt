import android.R.attr.text
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.team6.uicomponents.KindergartenItem
import com.example.team6.viewmodel.MainViewModel

@Composable
fun ClickTest(viewModel: MainViewModel = viewModel()) {
    val kindergartenList by viewModel.kindergartenList.collectAsState()
    if (kindergartenList.isEmpty()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("유치원 데이터를 불러오는 중입니다...")
        }
        return
    }
    val kinderInfo = kindergartenList[0]
    val sidoSggCodeMap = viewModel.nameToMapCode
    var sido = ""
    var sgg = ""
    for ((sidoCandidate, sggCandidate) in sidoSggCodeMap.keys) {
        if (kinderInfo.addr.contains(sidoCandidate) && kinderInfo.addr.contains(sggCandidate)) {
            sido = sidoCandidate
            sgg = sggCandidate
            break
        }
    }
    val clickData by viewModel.clickdata.collectAsState()

    Log.i("MainViewModelAPI","sfd")
    LaunchedEffect(sido, sgg, kinderInfo.kindername) {
       viewModel.populateClickData(sido, sgg, kinderInfo.kindername)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("유치원 이름: ${clickData.name}")
        Text("주소: ${clickData.address}")
        Text("전화번호: ${clickData.phone}")
        Text("CCTV 총계: ${clickData.cctv_ist_total}개")
        Text("교실 수: ${clickData.roomCount}개")
        Text("놀이터 여부: ${clickData.plyg_ck_yn}")
        Text("총 정원: ${clickData.totalCapacity}명")
        Text("현재 원아수: ${clickData.current}명")
        Text("교직원 수: ${clickData.staffCount}명")
        Text("통학차량 운영 여부: ${clickData.vhcl_oprn_yn}")
        Text("홈페이지: ${clickData.homepage}")
    }

}