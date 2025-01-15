package com.example.myapplication.data.repositories

import com.example.myapplication.api.ApiService
import com.example.myapplication.data.models.AddressResponse
import retrofit2.Response
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAddresses(): Response<List<AddressResponse>> {
        return apiService.getAddresses()
    }
}
