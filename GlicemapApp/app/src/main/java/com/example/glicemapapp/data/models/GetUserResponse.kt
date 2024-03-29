package com.example.glicemapapp.data.models

import com.google.gson.annotations.SerializedName

data class GetUserResponse (
    @SerializedName("user")
    val user: User,
    @SerializedName("medic")
    val medic: Doctor
)