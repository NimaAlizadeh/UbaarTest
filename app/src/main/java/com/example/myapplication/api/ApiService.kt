package com.example.myapplication.api

import com.example.myapplication.data.models.AddressRequest
import com.example.myapplication.data.models.AddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("karfarmas/address")
    suspend fun createAddress(@Body request: AddressRequest): Response<AddressResponse>

    @GET("karfarmas/address")
    suspend fun getAddresses(): Response<List<AddressResponse>>

}