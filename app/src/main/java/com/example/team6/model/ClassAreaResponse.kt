package com.example.team6.model

import com.google.gson.annotations.SerializedName

data class ClassAreaResponse(
    val status: String,
    @SerializedName("kinderInfo")
    val ClassArea: List<ClassArea>?
)
