package com.example.myapplication.data.models

data class AddressRequest(
    val region: Int,
    val address: String,
    val lat: Float,
    val lng: Float,
    val coordinate_mobile: String,
    val coordinate_phone_number: String,
    val first_name: String,
    val last_name: String,
    val gender: String
)