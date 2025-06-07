package com.example.team6.model

import com.google.gson.annotations.SerializedName

data class TeacherResponse(
    val status: String,
    @SerializedName("kinderInfo")
    val TeacherInfo: List<TeacherInfo>?
)

