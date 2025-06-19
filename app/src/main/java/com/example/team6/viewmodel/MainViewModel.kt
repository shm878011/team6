package com.example.team6.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.team6.model.BasicInfo
import com.example.team6.model.Click
import com.example.team6.model.KinderInfo // KinderInfo 사용
import com.example.team6.model.SchoolBusInfo // SchoolBusInfo 사용
import com.example.team6.model.SafeInfo
import com.example.team6.network.KindergartenApiService
import com.example.team6.network.RetrofitClient
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.poi.ss.usermodel.CellType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.collections.List
import kotlin.collections.filter

import android.location.Location

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    var Rangelocation: Double = -10.0


    val likedNurseries = mutableStateListOf<KinderInfo>()

    fun toggleLike(nursery: KinderInfo) {
        if (likedNurseries.contains(nursery)) {
            likedNurseries.remove(nursery)
        } else {
            likedNurseries.add(nursery)
        }
    }

    fun isLiked(nursery: KinderInfo): Boolean {
        Log.d(TAG, likedNurseries.toString())
        Log.d(TAG, nursery.toString())
        return likedNurseries.contains(nursery)
    }
    private val CURRENT_LOCATION_LAT_KEY = "current_location_latitude"
    private val CURRENT_LOCATION_LNG_KEY = "current_location_longitude"
    private val DEFAULT_LATITUDE = 37.5408
    private val DEFAULT_LONGITUDE = 127.0793

    var currentLocation by mutableStateOf<LatLng?>(null)
        private set

    private val _addressText = MutableStateFlow("서울특별시 광진구 능동로 120 건국대학교")
    val addressText: StateFlow<String> = _addressText

    fun updateLocation(latLng: LatLng) {
        currentLocation = latLng
        with(sharedPrefs.edit()) {
            putFloat(CURRENT_LOCATION_LAT_KEY, latLng.latitude.toFloat())
            putFloat(CURRENT_LOCATION_LNG_KEY, latLng.longitude.toFloat())
            apply()
        }
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

    private val _clicklist = MutableStateFlow<KinderInfo?>(null)
    val clicklist: StateFlow<KinderInfo?> = _clicklist

    fun setClickList(info: KinderInfo) {
        _clicklist.value = info
    }

    fun clearClickList() {
        _clicklist.value = null
    }

    data class SchoolZone(val latitude: Double, val longitude: Double)

    private val _schoolZones = mutableStateListOf<SchoolZone>()
    val schoolZones: List<SchoolZone> get() = _schoolZones
    // 마커 클릭 후 300m 이내 보호구역 필터링 결과 저장용
    private val _nearbyZones = MutableStateFlow<List<SchoolZone>>(emptyList())
    val nearbyZones: StateFlow<List<SchoolZone>> = _nearbyZones

    fun updateNearbyZones(centerLat: Double, centerLng: Double) {
        viewModelScope.launch(Dispatchers.Default) {
            val filtered = _schoolZones.filter {
                haversine(it.latitude, it.longitude, centerLat, centerLng) <= 2
            }
            _nearbyZones.value = filtered

            Log.d("NearbyZones", "유치원 기준 반경 2000m 내 보호구역: ${filtered.size}개") //
        }
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // 지구 반지름 (m)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2.0) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2).pow(2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }

    // CSV 로드 함수
    fun loadSchoolZones(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("school_zone.csv")
                val reader = BufferedReader(InputStreamReader(inputStream)) // UTF-8 기본 사용

                reader.useLines { lines ->
                    lines.drop(1).forEach { line ->
                        val tokens = line.split(",")
                        val lat = tokens.getOrNull(4)?.toDoubleOrNull()
                        val lng = tokens.getOrNull(5)?.toDoubleOrNull()
                        if (lat != null && lng != null) {
                            _schoolZones.add(SchoolZone(lat, lng))
                        }
                    }
                }

                Log.d("SchoolZone", " 어린이보호구역 로드 완료: ${_schoolZones.size}개")

            } catch (e: Exception) {
                Log.e("SchoolZone", " CSV 파싱 실패: ${e.message}")
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

    private val _kindergartensWithSafePlayground = MutableStateFlow<List<SafeInfo>>(emptyList())
    val kindergartensWithSafePlayground: StateFlow<List<SafeInfo>> = _kindergartensWithSafePlayground

    private val _kindergartensWithCCTV = MutableStateFlow<List<SafeInfo>>(emptyList())
    val kindergartensWithCCTV: StateFlow<List<SafeInfo>> = _kindergartensWithCCTV

    private var _kindergartenBasicList = MutableStateFlow<List<BasicInfo>>(emptyList())

    private var _Canadmission = false

    private val _checklist = MutableStateFlow<List<KinderInfo>>(emptyList())
    val checklist: StateFlow<List<KinderInfo>> = _checklist

    private val _clickdata = MutableStateFlow( Click(
        name = "",
        address = "",
        phone = "",
        cctv_ist_total = 0,
        roomCount = 0,
        plyg_ck_yn = "",
        totalCapacity = 0,
        current = 0,
        staffCount = 0,
        vhcl_oprn_yn = "",
        homepage = ""
    ))
    val clickdata: StateFlow<Click> = _clickdata

    private val kindergartenApiService: KindergartenApiService

    private val allSidoSggCodes: MutableMap<String, MutableList<String>> = mutableMapOf()

    private val nameToCodeMap: MutableMap<Pair<String, String>, Pair<String, String>> = mutableMapOf()
    val nameToMapCode: Map<Pair<String, String>, Pair<String, String>>
        get() = nameToCodeMap



    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val savedLat = sharedPrefs.getFloat(CURRENT_LOCATION_LAT_KEY, DEFAULT_LATITUDE.toFloat()).toDouble()
        val savedLng = sharedPrefs.getFloat(CURRENT_LOCATION_LNG_KEY, DEFAULT_LONGITUDE.toFloat()).toDouble()
        currentLocation = LatLng(savedLat, savedLng)


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
        fetchAllKindergartenData()
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
                    rowIterator.next() // 헤더 스킵
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
                            CellType.NUMERIC -> sidoCodeCell.numericCellValue.toInt().toString()
                            else -> sidoCodeCell.toString().trim()
                        }
                        val sggName = sggNameCell.toString().trim()
                        val sggCode = when (sggCodeCell.cellType) {
                            CellType.NUMERIC -> sggCodeCell.numericCellValue.toInt().toString()
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

    // CSV 파일에서 모든 유치원 데이터를 불러와 _kindergartenList에 저장
    fun fetchAllKindergartenData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "CSV 파일 로드 시작")

            val kindergartens = mutableListOf<KinderInfo>()
            try {
                val inputStream: InputStream = getApplication<Application>().assets.open("data.csv")
                val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))

                reader.useLines { lines ->
                    val dataLines = lines.drop(1) // 헤더 스킵
                    dataLines.forEach { line ->
                        val columns = line.split(",").map { it.trim() }

                        try {
                            // CSV에서 KinderInfo 객체 생성
                            val kinderInfo = KinderInfo(
                                kindername = columns.getOrElse(2) { "" },
                                addr = columns.getOrElse(8) { "" },
                                telno = columns.getOrElse(9) { "" },
                                hpaddr = columns.getOrElse(11) { "" },
                                opertime = columns.getOrElse(12) { "" },
                                ldgrname = columns.getOrElse(5) { "" },
                                latitude = columns.getOrElse(29) { "0.0" }.toDoubleOrNull() ?: 0.0,
                                longitude = columns.getOrElse(30) { "0.0" }.toDoubleOrNull() ?: 0.0,

                                totalCapacity = columns.getOrElse(18){ "" }.toIntOrNull() ?: 0, // 인가총정원수
                                current3yrOlds = columns.getOrElse(24) { "" }.toIntOrNull() ?: 0, // 만3세원아수
                                current4yrOlds = columns.getOrElse(25) { "" }.toIntOrNull() ?: 0, // 만4세원아수
                                current5yrOlds = columns.getOrElse(26) { "" }.toIntOrNull() ?: 0, // 만5세원아수
                                currentMixedOlds = columns.getOrElse(27) { "" }.toIntOrNull() ?: 0, // 혼합원아수
                                currentSpecialNeedsOlds = columns.getOrElse(28) { "" }.toIntOrNull() ?: 0 // 특수원아수
                            )
                            kindergartens.add(kinderInfo)
                        } catch (e: Exception) {
                            Log.e(TAG, "CSV 한 줄 파싱 오류: $line, 에러: ${e.message}")
                        }
                    }
                }
                inputStream.close()

                withContext(Dispatchers.Main) {
                    _kindergartenList.value = kindergartens
                    Log.d(TAG, "CSV 파일 로드 완료, 총 ${kindergartens.size}개 유치원 데이터")
                    updateChecklist()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "CSV 파일 로드 실패: ${e.message}", e)
                }
            }
        }
    }

    suspend fun fetchKindergartenData(sidoName: String, sggName: String) {
        val codes = getSidoSggCodesByName(sidoName, sggName)

        if (codes == null) {
            Log.e(TAG, "해당 시도명($sidoName)과 시군구명($sggName)에 대한 코드를 찾을 수 없습니다.")
            _kindergartenBasicList.value = emptyList()
            return
        }

        val (sidoCode, sggCode) = codes
        try {
            val response = kindergartenApiService.getKindergartenBasicInfo(YOUR_API_KEY, sidoCode, sggCode)

            if (response.status == "SUCCESS") {
                Log.d(TAG, "API 호출 성공 (${sidoCode}-${sggCode}), 데이터 수: ${response.kinderInfo?.size ?: 0}")
                _kindergartenBasicList.value = response.kinderInfo ?: emptyList()
            } else {
                Log.e(TAG, "API 응답 실패 (${sidoCode}-${sggCode}): ${response.status}")
                _kindergartenBasicList.value = emptyList()
            }

        } catch (e: Exception) {
            Log.e(TAG, "네트워크 또는 파싱 오류 (${sidoCode}-${sggCode}): ${e.message}", e)
            _kindergartenBasicList.value = emptyList()
        }
    }


    fun getSidoSggCodesByName(sidoName: String, sggName: String): Pair<String, String>? {
        return nameToCodeMap[Pair(sidoName, sggName)]
    }

    suspend fun fetchKindergartensWithSchoolBus(sidoName: String, sggName: String) {
        val codes = getSidoSggCodesByName(sidoName, sggName)
        if (codes == null) {
            Log.e(TAG, "해당 시도명($sidoName)과 시군구명($sggName)에 대한 코드를 찾을 수 없습니다.")
            _schoolBusKindergartens.value = emptyList() // StateFlow 업데이트 (withContext 불필요)
            return
        }

        val (sidoCode, sggCode) = codes

        try {
            val response = kindergartenApiService.getKindergartenSchoolBusInfo(YOUR_API_KEY, sidoCode, sggCode)

            if (response.status == "SUCCESS") {
                val filteredList = response.schoolBusInfo
                    ?.filterNotNull()
                    ?.filter { it.vhcl_oprn_yn == "Y" }
                    ?: emptyList()

                _schoolBusKindergartens.value = filteredList
                Log.d(TAG, "통학차량('Y') 유치원 목록 로드 성공 (${sidoName}-${sggName}), 데이터 수: ${filteredList.size}")
            } else {
                Log.e(TAG, "통학차량 API 응답 실패 (${sidoName}-${sggCode}): ${response.status}")
                _schoolBusKindergartens.value = emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "통학차량 API 네트워크 또는 파싱 오류 (${sidoName}-${sggCode}): ${e.message}", e)
            _schoolBusKindergartens.value = emptyList()
        }
    }

    fun updateChecklist() {
        viewModelScope.launch {
            Log.d(TAG, "시작")
            var newChecklist: List<KinderInfo> = kindergartenList.value
            var changelist: List<BasicInfo> = _kindergartenBasicList.value
            Log.d(TAG,"chagelist ${changelist}")
            if (changelist.isNotEmpty() && schoolBusKindergartens.value.isNotEmpty()) {
                val schoolBusNames = schoolBusKindergartens.value.map { Pair(it.kindercode, it.kindername) }.toSet()
                changelist = changelist.filter { kinderInfo ->
                    Pair(kinderInfo.kindercode, kinderInfo.kindername) in schoolBusNames
                }
                Log.d(TAG,"schoolbusName ${schoolBusNames}")
                Log.d(TAG, "Checklist 업데이트 (모드: 통학버스): ${changelist.size}개 유치원 매칭.")
            } else {
                Log.d(TAG, "Checklist 업데이트(통학버스): 원본 리스트 중 하나 이상이 비어있음.")
            }
            if (changelist.isNotEmpty() && kindergartensWithSafePlayground.value.isNotEmpty()) {
                val safePlaygroundKinderNames = kindergartensWithSafePlayground.value
                    .filter { it.plyg_ck_yn == "Y" }
                    .map { Pair(it.kindercode, it.kindername) }
                    .toSet()
                changelist = changelist.filter { kinderInfo ->
                    Pair(kinderInfo.kindercode, kinderInfo.kindername) in safePlaygroundKinderNames
                }
                Log.d(TAG, "Checklist 업데이트 (모드: 놀이터 안전): ${changelist.size}개 유치원 매칭.")
            } else {
                Log.d(TAG, "Checklist 업데이트(놀이터 안전): 원본 리스트 중 하나 이상이 비어있음.")
            }
            if (changelist.isNotEmpty() && _kindergartensWithCCTV.value.isNotEmpty()) {
                val CCTVKinderNames = _kindergartensWithCCTV.value
                    .filter { it.cctv_ist_yn == "Y" }
                    .map { Pair(it.kindercode, it.kindername) }
                    .toSet()
                changelist = changelist.filter { kinderInfo ->
                    Pair(kinderInfo.kindercode, kinderInfo.kindername) in CCTVKinderNames
                }
                Log.d(TAG, "Checklist 업데이트 (모드: 놀이터 안전): ${changelist.size}개 유치원 매칭.")
            } else {
                Log.d(TAG, "Checklist 업데이트(놀이터 안전): 원본 리스트 중 하나 이상이 비어있음.")
            }
            if (changelist.isNotEmpty()){
                val CHANGE = changelist.map { Pair(it.kindername, it.addr) }.toSet()
                newChecklist = newChecklist.filter { kinderInfo ->
                    Pair(kinderInfo.kindername, kinderInfo.addr) in CHANGE
                }
            }

            if(newChecklist.isNotEmpty() && _Canadmission){
                newChecklist = newChecklist.filter { KinderInfo->
                    KinderInfo.totalCapacity > KinderInfo.current
                }
            }

            if(newChecklist.isNotEmpty() && Rangelocation > 0){
                currentLocation?.let { userLocation ->
                    val rangeInMeters = Rangelocation * 1000
                    newChecklist = newChecklist.filter { kinderInfo ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            userLocation.latitude,
                            userLocation.longitude,
                            kinderInfo.latitude ?: 0.0,
                            kinderInfo.longitude ?: 0.0,
                            results
                        )
                        val distanceInMeters = results[0]
                        distanceInMeters <= rangeInMeters
                    }
                    Log.d(TAG, "Checklist 업데이트 (거리 필터링 ${Rangelocation}km 이내): ${newChecklist.size}개 유치원 매칭.")
                } ?: run {
                    Log.d(TAG, "Checklist 업데이트(거리 필터링): currentLocation이 null이므로 거리 필터링을 건너뜁니다.")
                }
            }

            _checklist.value = newChecklist
            Log.d(TAG, "최종 Checklist 업데이트 완료: ${newChecklist.size}개 유치원.")
        }
    }

    suspend fun fetchKindergartensWithSafePlayground(sidoName: String, sggName: String) {
        val codes = getSidoSggCodesByName(sidoName, sggName)
        if (codes == null) {
            Log.e(TAG, "해당 시도명($sidoName)과 시군구명($sggName)에 대한 코드를 찾을 수 없습니다.")
            _kindergartensWithSafePlayground.value = emptyList()
            return
        }

        val (sidoCode, sggCode) = codes

        try {
            val response = kindergartenApiService.getKindergartenSafetyInfo(YOUR_API_KEY, sidoCode, sggCode)

            if (response.status == "SUCCESS") {
                val filteredAndSummarizedList = response.safeInfo
                    ?.filterNotNull()
                    ?.filter { it.plyg_ck_yn == "Y" }
                    ?: emptyList()

                _kindergartensWithSafePlayground.value = filteredAndSummarizedList
                Log.d(TAG, "놀이터 안전점검('Y') 유치원 목록 로드 성공 (${sidoName}-${sggName}), 데이터 수: ${filteredAndSummarizedList.size}")
            } else {
                Log.e(TAG, "안전 정보 API 응답 실패 (${sidoName}-${sggCode}): ${response.status}")
                _kindergartensWithSafePlayground.value = emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "안전 정보 API 네트워크 또는 파싱 오류 (${sidoName}-${sggCode}): ${e.message}", e)
            _kindergartensWithSafePlayground.value = emptyList()
        }
    }

    suspend fun fetchKindergartensWithSafeCCTV(sidoName: String, sggName: String) {
        val codes = getSidoSggCodesByName(sidoName, sggName)
        if (codes == null) {
            Log.e(TAG, "해당 시도명($sidoName)과 시군구명($sggName)에 대한 코드를 찾을 수 없습니다.")
            _kindergartensWithCCTV.value = emptyList()
            return
        }

        val (sidoCode, sggCode) = codes

        try {
            val response = kindergartenApiService.getKindergartenSafetyInfo(YOUR_API_KEY, sidoCode, sggCode)

            if (response.status == "SUCCESS") {
                val filteredAndSummarizedList = response.safeInfo
                    ?.filterNotNull()
                    ?.filter { it.cctv_ist_yn == "Y" }
                    ?: emptyList()

                _kindergartensWithCCTV.value = filteredAndSummarizedList
                Log.d(TAG, "CCTV('Y') 유치원 목록 로드 성공 (${sidoName}-${sggName}), 데이터 수: ${filteredAndSummarizedList.size}") // 로그 메시지 수정
            } else {
                Log.e(TAG, "안전 정보 API 응답 실패 (${sidoName}-${sggCode}): ${response.status}")
                _kindergartensWithCCTV.value = emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "안전 정보 API 네트워크 또는 파싱 오류 (${sidoName}-${sggCode}): ${e.message}", e)
            _kindergartensWithCCTV.value = emptyList()
        }
    }

    fun populateClickData(sidoName: String, sggName: String, kindername: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "ClickData 업데이트 시작: 유치원명=$kindername, 시도=$sidoName, 시군구=$sggName")

            val codes = getSidoSggCodesByName(sidoName, sggName)
            if (codes == null) {
                Log.e(TAG, "시도/시군구 코드를 찾을 수 없습니다: $sidoName, $sggName")
                withContext(Dispatchers.Main) {
                    _clickdata.value = Click(
                        name = kindername,
                        address = "주소 정보 없음",
                        phone = "전화 정보 없음",
                        cctv_ist_total = 0,
                        roomCount = 0,
                        plyg_ck_yn = "정보 없음",
                        totalCapacity = 0,
                        current = 0,
                        staffCount = 0,
                        vhcl_oprn_yn = "정보 없음",
                        homepage = "홈페이지 정보 없음"
                    )
                }
                return@launch
            }
            val (sidoCode, sggCode) = codes

            val basicKinderInfo = _kindergartenList.value.firstOrNull { kinderInfo ->
                kinderInfo.kindername == kindername &&
                        kinderInfo.addr.contains(sidoName) && // 주소에 sidoName 포함 여부 확인
                        kinderInfo.addr.contains(sggName)     // 주소에 sggName 포함 여부 확인
            }

            var clickName = basicKinderInfo?.kindername ?: kindername
            var clickAddress = basicKinderInfo?.addr ?: "주소 정보 없음"
            var clickPhone = basicKinderInfo?.telno ?: "전화 정보 없음"
            var clickCctvTotal = 0
            var clickRoomCount = 0
            var clickPlygCkYn = "N"
            var clickTotalCapacity = basicKinderInfo?.totalCapacity ?: 0
            var clickCurrentStudents = basicKinderInfo?.current ?: 0
            var clickStaffCount = 0
            var clickVhclOprnYn = "N"
            var clickHomepage = basicKinderInfo?.hpaddr ?: "홈페이지 정보 없음"

            val safeInfoDeferred = async {
                try {
                    kindergartenApiService.getKindergartenSafetyInfo(YOUR_API_KEY, sidoCode, sggCode)
                } catch (e: Exception) {
                    Log.e(TAG, "안전 정보 API 호출 오류: ${e.message}")
                    null
                }
            }
            val schoolBusInfoDeferred = async {
                try {
                    kindergartenApiService.getKindergartenSchoolBusInfo(YOUR_API_KEY, sidoCode, sggCode)
                } catch (e: Exception) {
                    Log.e(TAG, "통학차량 정보 API 호출 오류: ${e.message}")
                    null
                }
            }
            val teacherInfoDeferred = async {
                try {
                    kindergartenApiService.getKindergartenTeachersInfo(YOUR_API_KEY, sidoCode, sggCode)
                } catch (e: Exception) {
                    Log.e(TAG, "교직원 정보 API 호출 오류: ${e.message}")
                    null
                }
            }
            val classAreaInfoDeferred = async {
                try {
                    kindergartenApiService.getKindergartenclassArea(YOUR_API_KEY, sidoCode, sggCode)
                } catch (e: Exception) {
                    Log.e(TAG, "시설 정보 API 호출 오류: ${e.message}")
                    null
                }
            }

            val safeResponse = safeInfoDeferred.await()
            safeResponse?.safeInfo?.firstOrNull { it.kindername == kindername }?.let { safeInfo ->
                clickCctvTotal = safeInfo.cctv_ist_total?.replace("개", "")?.toIntOrNull() ?: 0
                clickPlygCkYn = safeInfo.plyg_ck_yn ?: "N"
            }

            val schoolBusResponse = schoolBusInfoDeferred.await()
            schoolBusResponse?.schoolBusInfo?.firstOrNull { it.kindername == kindername }?.let { schoolBusInfo ->
                clickVhclOprnYn = schoolBusInfo.vhcl_oprn_yn ?: "N"
            }

            val teacherResponse = teacherInfoDeferred.await()
            teacherResponse?.TeacherInfo?.firstOrNull { it.kindername == kindername }?.let { teacherInfo ->
                clickStaffCount = (teacherInfo.drcnt ?: 0) +
                        (teacherInfo.adcnt ?: 0) +
                        (teacherInfo.hdst_thcnt ?: 0) +
                        (teacherInfo.asps_thcnt ?: 0) +
                        (teacherInfo.gnrl_thcnt ?: 0) +
                        (teacherInfo.spcn_thcnt ?: 0) +
                        (teacherInfo.ntcnt ?: 0) +
                        (teacherInfo.ntrt_thcnt ?: 0) +
                        (teacherInfo.shcnt_thcnt ?: 0) +
                        (teacherInfo.owcnt ?: 0)
            }

            val classAreaResponse = classAreaInfoDeferred.await()
            classAreaResponse?.ClassArea?.firstOrNull { it.kindername == kindername }?.let { classAreaInfo ->
                clickRoomCount = classAreaInfo.crcnt?.replace("개", "")?.toIntOrNull() ?: 0
            }

            withContext(Dispatchers.Main) {
                _clickdata.value = Click(
                    name = clickName,
                    address = clickAddress,
                    phone = clickPhone,
                    cctv_ist_total = clickCctvTotal,
                    roomCount = clickRoomCount,
                    plyg_ck_yn = clickPlygCkYn,
                    totalCapacity = clickTotalCapacity,
                    current = clickCurrentStudents,
                    staffCount = clickStaffCount,
                    vhcl_oprn_yn = clickVhclOprnYn,
                    homepage = clickHomepage
                )
                Log.d(TAG, "ClickData 업데이트 완료: $_clickdata.value")
            }
        }
    }

    fun RemoveBus(){
        viewModelScope.launch(Dispatchers.Main) {
            _schoolBusKindergartens.value = emptyList()
            Log.d(TAG, "체크리스트가 비워졌습니다.")
        }
    }

    fun RemovePlayground(){
        viewModelScope.launch(Dispatchers.Main) {
            _kindergartensWithSafePlayground.value = emptyList()
            Log.d(TAG, "체크리스트가 비워졌습니다.")
        }
    }

    fun RemoveCCTV(){
        viewModelScope.launch(Dispatchers.Main) {
            _kindergartensWithCCTV.value = emptyList()
            Log.d(TAG, "체크리스트가 비워졌습니다.")
        }
    }

    fun Findlist(value: String){
        _checklist.value = _kindergartenList.value.filter {
            it.kindername.contains(value, ignoreCase = true)
        }
    }

    fun Canadmission(bool: Boolean) {
        _Canadmission = bool
    }

    fun changedistance(selectedDistance: String) {
        if(selectedDistance == "1km"){
            Rangelocation = 1.0
        }
        else if(selectedDistance == "2km"){
            Rangelocation = 2.0
        }
        else if(selectedDistance == "4km"){
            Rangelocation = 4.0
        }
        else if(selectedDistance == "10km"){
            Rangelocation = 10.0
        }
    }

}