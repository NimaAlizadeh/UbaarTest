package com.example.myapplication.data.repositories

import android.util.Log
import com.example.myapplication.api.ApiService
import com.example.myapplication.data.models.AddressRequest
import com.example.myapplication.data.models.AddressResponse
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun createAddress(request: AddressRequest): Response<AddressResponse> {
        Log.d("RegisterRepository", "Calling API to create address with: $request")
        return apiService.createAddress(request).also {
            Log.d("RegisterRepository", "API Response: ${it.body()} - Status Code: ${it.code()}")
        }
    }
}
