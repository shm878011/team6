package com.example.team6.network

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

@Composable
fun GeocodingTestScreen() {
    val coroutineScope = rememberCoroutineScope()
    var addressInput by remember { mutableStateOf(TextFieldValue("")) }
    var coordInput by remember { mutableStateOf(TextFieldValue("")) }
    var resultText by remember { mutableStateOf("결과 출력란") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("주소 → 좌표 (Geocoding)")
        OutlinedTextField(
            value = addressInput,
            onValueChange = { addressInput = it },
            label = { Text("주소 입력 (예: 서울시청)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val response = RetrofitClient.geocodingApi.getGeocode(addressInput.text)
                        if (response.isSuccessful) {
                            val body = response.body()
                            val address = body?.addresses?.firstOrNull()
                            resultText = address?.let {
                                "위도: ${it.y}\n경도: ${it.x}"
                            } ?: "주소를 찾을 수 없습니다."
                        } else {
                            resultText = "요청 실패: ${response.code()}"
                            Log.e("GeocodeTest", "요청 실패: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        resultText = "에러 발생: ${e.message}"
                        Log.e("GeocodeTest", "에러: ", e)
                    }
                }
            }
        ) {
            Text("Geocoding 요청")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("좌표 → 주소 (Reverse Geocoding)")
        OutlinedTextField(
            value = coordInput,
            onValueChange = { coordInput = it },
            label = { Text("좌표 입력 (예: 126.9784,37.5666)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val coords = coordInput.text.replace(" ", "")
                        val response = RetrofitClient.reverseGeocodingApi.getAddress(coords)
                        if (response.isSuccessful) {
                            val body = response.body()
                            val result = body?.results?.firstOrNull()
                            val area1 = result?.region?.area1?.name ?: ""
                            val area2 = result?.region?.area2?.name ?: ""
                            val area3 = result?.region?.area3?.name ?: ""
                            resultText = "$area1 $area2 $area3"
                        } else {
                            resultText = "요청 실패: ${response.code()}"
                            Log.e("ReverseGeocodeTest", "요청 실패: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        resultText = "에러 발생: ${e.message}"
                        Log.e("ReverseGeocodeTest", "에러: ", e)
                    }
                }
            }
        ) {
            Text("Reverse Geocoding 요청")
        }

        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
        Text(resultText)
    }
}