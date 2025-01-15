package com.example.myapplication.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.AddressRequest
import com.example.myapplication.data.models.AddressResponse
import com.example.myapplication.data.repositories.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) : ViewModel() {

    private val _creationStatus = MutableLiveData<Boolean>()
    val creationStatus: LiveData<Boolean> = _creationStatus

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    private val _errorStatus = MutableLiveData<String?>()
    val errorStatus: LiveData<String?> = _errorStatus

    fun createAddress(request: AddressRequest) {
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val response = repository.createAddress(request)
                if (response.isSuccessful) {
                    _creationStatus.postValue(true)
                } else {
                    _errorStatus.postValue("خطا در ثبت آدرس. لطفاً دوباره تلاش کنید.")
                }
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Error creating address", e)
                _errorStatus.postValue("خطا در اتصال به اینترنت. لطفاً دوباره تلاش کنید.")
            } finally {
                _loading.postValue(false)
            }
        }
    }
}
