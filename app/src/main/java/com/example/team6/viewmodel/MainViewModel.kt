package com.example.team6.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.team6.model.*
import com.example.team6.network.KindergartenApiService
import com.example.team6.network.RetrofitClient
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.delay
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val likedNurseries = mutableStateListOf<Nursery>()

    fun toggleLike(nursery: Nursery) {
        if (likedNurseries.contains(nursery)) {
            likedNurseries.remove(nursery)
        } else {
            likedNurseries.add(nursery)
        }
    }

    fun isLiked(nursery: Nursery): Boolean {
        return likedNurseries.contains(nursery)
    }

    var currentLocation by mutableStateOf<LatLng?>(null)
        private set

    private val _addressText = MutableStateFlow("서울특별시 광진구 능동로 120 건국대학교")
    val addressText: StateFlow<String> = _addressText

    fun updateLocation(latLng: LatLng) {
        currentLocation = latLng
        fetchAddressFromCoords(latLng)
    }

    fun fetchAddressFromCoords(latLng: LatLng) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.reverseGeocodingApi.getAddress(
                    coords = "${latLng.longitude},${latLng.latitude}"
                )
                if (response.isSuccessful) {
                    val region = response.body()?.results?.firstOrNull()?.region
                    val fullAddress = listOfNotNull(
                        region?.area1?.name,
                        region?.area2?.name,
                        region?.area3?.name
                    ).joinToString(" ")
                    _addressText.value = fullAddress
                } else {
                    _addressText.value = "주소를 찾을 수 없습니다"
                }
            } catch (e: Exception) {
                _addressText.value = "주소를 찾을 수 없습니다"
            }
        }
    }

    fun searchAddress(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.geocodingApi.getGeocode(query)
                if (response.isSuccessful) {
                    val addr = response.body()?.addresses?.firstOrNull()
                    if (addr != null && addr.x != null && addr.y != null) {
                        val latLng = LatLng(addr.y!!.toDouble(), addr.x!!.toDouble())
                        updateLocation(latLng)
                    } else {
                        _addressText.value = "주소를 찾을 수 없습니다"
                    }
                } else {
                    _addressText.value = "주소를 찾을 수 없습니다"
                }
            } catch (e: Exception) {
                _addressText.value = "주소를 찾을 수 없습니다"
            }
        }
    }

    private val TAG = "MainViewModelAPI"
    private val BASE_URL = "https://e-childschoolinfo.moe.go.kr/"
    private val YOUR_API_KEY = "cf749df1e1224d0c80be9efc008279ad"

    private val _kindergartenList = MutableStateFlow<List<KinderInfo>>(emptyList())
    val kindergartenList: StateFlow<List<KinderInfo>> = _kindergartenList

    private val _schoolBusKindergartens = MutableStateFlow<List<SchoolBusInfo>>(emptyList())
    val schoolBusKindergartens: StateFlow<List<SchoolBusInfo>> = _schoolBusKindergartens

    private val kindergartenApiService: KindergartenApiService

    private val allSidoSggCodes: MutableMap<String, MutableList<String>> = mutableMapOf()

    private val nameToCodeMap: MutableMap<Pair<String, String>, Pair<String, String>> = mutableMapOf()

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        kindergartenApiService = retrofit.create(KindergartenApiService::class.java)

        loadSidoSggCodesFromExcel()
    }

    private fun loadSidoSggCodesFromExcel() {
        viewModelScope.launch {
            try {
                val inputStream: InputStream = getApplication<Application>().assets.open("code.xlsx")
                val workbook = WorkbookFactory.create(inputStream)
                val sheet = workbook.getSheetAt(0)

                val tempSidoSggMap = mutableMapOf<String, MutableList<String>>()

                val rowIterator = sheet.iterator()
                if (rowIterator.hasNext()) {
                    rowIterator.next()
                }

                while (rowIterator.hasNext()) {
                    val row = rowIterator.next()

                    val sidoNameCell = row.getCell(0)
                    val sidoCodeCell = row.getCell(1)
                    val sggNameCell = row.getCell(2)
                    val sggCodeCell = row.getCell(3)

                    if (sidoNameCell != null && sidoCodeCell != null && sggNameCell != null && sggCodeCell != null) {
                        val sidoName = sidoNameCell.toString().trim()
                        val sidoCode = when (sidoCodeCell.cellType) {
                            org.apache.poi.ss.usermodel.CellType.NUMERIC -> sidoCodeCell.numericCellValue.toInt().toString()
                            else -> sidoCodeCell.toString().trim()
                        }
                        val sggName = sggNameCell.toString().trim()
                        val sggCode = when (sggCodeCell.cellType) {
                            org.apache.poi.ss.usermodel.CellType.NUMERIC -> sggCodeCell.numericCellValue.toInt().toString()
                            else -> sggCodeCell.toString().trim()
                        }

                        if (sidoCode.isNotBlank() && sggCode.isNotBlank()) {
                            tempSidoSggMap.getOrPut(sidoCode) { mutableListOf() }.add(sggCode)

                            if (sidoName.isNotBlank() && sggName.isNotBlank()) {
                                nameToCodeMap[Pair(sidoName, sggName)] = Pair(sidoCode, sggCode)
                            }
                        }
                    }
                }
                workbook.close()
                inputStream.close()
                allSidoSggCodes.putAll(tempSidoSggMap)
                Log.d(TAG, "시도/시군구 코드 로드 완료: ${allSidoSggCodes.keys.size}개 시도, 총 ${allSidoSggCodes.values.sumOf { it.size }}개 시군구")
                Log.d(TAG, "이름-코드 매핑 로드 완료: ${nameToCodeMap.size}개 항목")

            } catch (e: Exception) {
                Log.e(TAG, "시도/시군구 엑셀 코드 로드 실패: ${e.message}", e)
            }
        }
    }

    fun fetchKindergartenData(sidoCode: String, sggCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = kindergartenApiService.getKindergartenBasicInfo(BASE_URL, sidoCode, sggCode)

                if (response.status == "SUCCESS") {
                    withContext(Dispatchers.Main) {
                        Log.d("MainViewModelAPI", "API 호출 성공 (${sidoCode}-${sggCode}), 데이터 수: ${response.kinderInfo.size}")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("MainViewModelAPI", "API 응답 실패 (${sidoCode}-${sggCode}): ${response.status}")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MainViewModelAPI", "네트워크 또는 파싱 오류 (${sidoCode}-${sggCode}): ${e.message}", e)
                }
            }
        }
    }

    fun fetchAllKindergartenData() {
        viewModelScope.launch {
            if (allSidoSggCodes.isEmpty()) {
                Log.w(TAG, "시도/시군구 코드가 로드되지 않았습니다. 먼저 loadSidoSggCodesFromExcel()를 호출하세요.")
                return@launch
            }

            val fetchedKinderList = mutableListOf<KinderInfo>()

            allSidoSggCodes.forEach { (sidoCode, sggCodeList) ->
                sggCodeList.forEach { sggCode ->
                    Log.d(TAG, "fetching data for Sido Code: $sidoCode, Sgg Code: $sggCode")
                    try {
                        val response = kindergartenApiService.getKindergartenBasicInfo(YOUR_API_KEY, sidoCode, sggCode)
                        if (response.status == "SUCCESS") {
                            val enrichedKinderInfoList = response.kinderInfo.map { kinderInfo ->
                                kinderInfo.copy(
                                    sidoCode = sidoCode,
                                    sggCode = sggCode
                                )
                            }
                            fetchedKinderList.addAll(enrichedKinderInfoList)
                            Log.d(TAG, "API 호출 성공 (${sidoCode}-${sggCode}), 데이터 수: ${response.kinderInfo.size}")
                        } else {
                            Log.e(TAG, "API 응답 실패 (${sidoCode}-${sggCode}): ${response.status}")
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "네트워크 또는 파싱 오류 (${sidoCode}-${sggCode}): ${e.message}", e)
                    }
                }
            }
            _kindergartenList.value = fetchedKinderList.toList()
            Log.d(TAG, "모든 지역 데이터 호출 완료. 총 ${fetchedKinderList.size}개 유치원 정보 로드됨.")
        }
    }

    fun getSidoSggCodesByName(sidoName: String, sggName: String): Pair<String, String>? {
        return nameToCodeMap[Pair(sidoName, sggName)]
    }

    fun fetchKindergartensWithSchoolBus(sidoName: String, sggName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val codes = getSidoSggCodesByName(sidoName, sggName)
            if (codes == null) {
                Log.e(TAG, "해당 시도명($sidoName)과 시군구명($sggName)에 대한 코드를 찾을 수 없습니다.")
                _schoolBusKindergartens.value = emptyList()
                return@launch
            }

            val (sidoCode, sggCode) = codes

            try {
                val response = kindergartenApiService.getKindergartenSchoolBusInfo(YOUR_API_KEY, sidoCode, sggCode)

                if (response.status == "SUCCESS") {
                    val filteredList = response.schoolBusInfo.filter { it.schoolBusYn == "Y" }
                    withContext(Dispatchers.Main) {
                        _schoolBusKindergartens.value = filteredList
                        Log.d(TAG, "통학차량 운영('Y') 유치원 목록 로드 성공 (${sidoName}-${sggName}), 데이터 수: ${filteredList.size}")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e(TAG, "통학차량 API 응답 실패 (${sidoName}-${sggName}): ${response.status}")
                        _schoolBusKindergartens.value = emptyList()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "통학차량 API 네트워크 또는 파싱 오류 (${sidoName}-${sggName}): ${e.message}", e)
                    _schoolBusKindergartens.value = emptyList()
                }
            }
        }
    }
}
