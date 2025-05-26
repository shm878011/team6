package com.example.team6.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.team6.model.Nursery
import com.naver.maps.geometry.LatLng

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

    fun updateLocation(latLng: LatLng){
        currentLocation = latLng
    }

}
