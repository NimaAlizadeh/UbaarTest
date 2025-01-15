package com.example.myapplication.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.AddressResponse
import com.example.myapplication.data.repositories.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val repository: AddressRepository
) : ViewModel() {

    private val _addresses = MutableLiveData<List<AddressResponse>>()
    val addresses: LiveData<List<AddressResponse>> = _addresses

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // برای گرفتن لیست آدرس‌ها از سرور
    fun fetchAddresses() {
        viewModelScope.launch {
            _loading.postValue(true)

            try {
                val response: Response<List<AddressResponse>> = repository.getAddresses()
                if (response.isSuccessful) {
                    _addresses.value = response.body() ?: emptyList()
                } else {
                    // مدیریت سناریوی خطا (می‌توانید یک لاگ یا LiveData برای خطا بگذارید)
                    _addresses.value = emptyList()
                }
            } catch (e: Exception) {
                // مدیریت خطا (عدم دسترسی به اینترنت یا هر مشکل دیگر)
                _addresses.value = emptyList()
            }

            _loading.postValue(false)
        }
    }

}
