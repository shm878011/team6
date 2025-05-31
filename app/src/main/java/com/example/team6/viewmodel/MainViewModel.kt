package com.example.team6.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.team6.model.Nursery
import com.example.team6.network.RetrofitClient
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
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

}
