package com.example.myapplication.di

import com.example.myapplication.api.ApiService
import com.example.myapplication.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTime(): Long = Constants.CLIENT_TIMEOUT

    @Provides
    @Singleton
    fun provideClient(time: Long): OkHttpClient {
        val credentials = Credentials.basic("09822222222", "Sana12345678")

        return OkHttpClient.Builder()
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .connectTimeout(time, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", credentials)
                    .build()

                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient, gson: Gson): ApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)

}
