package com.example.team6.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.team6.model.Nursery

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
}
