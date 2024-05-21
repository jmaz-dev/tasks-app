package com.devmasterteam.tasks.service.repository.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {

        private const val BASE_URL = "http://devmasterteam.com/CursoAndroidAPI/"
        private lateinit var INSTANCE: Retrofit
        private fun getRetrofitInstance(): Retrofit {

            if (!::INSTANCE.isInitialized) {
                synchronized(Retrofit::class.java) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(OkHttpClient.Builder().build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }

            return INSTANCE

        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }
    }
}