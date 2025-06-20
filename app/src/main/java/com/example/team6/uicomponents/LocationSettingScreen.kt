package com.example.team6.uicomponents

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.team6.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationSettingScreen(navController: NavController, viewModel: MainViewModel) {
    SubPage(title = "위치 설정", navController = navController) {
        // 내 위치

        val address by viewModel.addressText.collectAsState()
        var searchText by remember { mutableStateOf("") }
        val context = LocalContext.current
        val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

        val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

        Text("내 위치", fontWeight = FontWeight.SemiBold)
        Text(address)

        Spacer(modifier = Modifier.height(24.dp))

        // 위치 수정
        Text("위치 수정", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            OutlinedTextField(
                value = searchText,
                onValueChange = {searchText = it},
                modifier = Modifier.weight(1f),
                placeholder = { Text("주소 검색") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    if (searchText.isNotBlank()) {
                        viewModel.searchAddress(searchText)
                    } else {
                        Toast.makeText(context, "주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("수정")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        LaunchedEffect(Unit) {
            if (permissionState.status is PermissionStatus.Denied) {
                val denied = permissionState.status as PermissionStatus.Denied
                if (denied.shouldShowRationale) {
                    permissionState.launchPermissionRequest()
                }
            }
        }
        // 현재 위치로 설정
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("현재 위치로 설정", modifier = Modifier.weight(1f))
            OutlinedButton(onClick = {
                when (val status = permissionState.status) {
                    is PermissionStatus.Granted -> {
                        // 권한 허용됨 → 현재 위치 가져와서 저장
                        fusedClient.lastLocation
                            .addOnSuccessListener { location ->
                                if (location != null) {
                                    viewModel.updateLocation(LatLng(location.latitude, location.longitude))
                                    Toast.makeText(context, "현재 위치로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "위치 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "위치 정보 획득 실패", Toast.LENGTH_SHORT).show()
                            }
                    }

                    is PermissionStatus.Denied -> {
                        if (status.shouldShowRationale) {
                            // 다시 물어볼 수 있음 → 권한 요청
                            permissionState.launchPermissionRequest()
                        } else {
                            // 다시 묻지 않음 or 첫 시도인데 거부됨
                            permissionState.launchPermissionRequest()
                            Toast.makeText(
                                context,
                                "위치 권한이 필요합니다. 거부하셨다면 설정에서 수동 허용해주세요.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }) {
                Text("설정")
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun LocationSettingPreview() {
//    LocationSettingScreen(navController = rememberNavController())
//}