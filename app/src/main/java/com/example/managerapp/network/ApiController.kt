package com.example.managerapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiController {

    companion object {

        private val url = "https://corporatelife.in/"

        // Create a custom OkHttpClient with timeout settings
        private val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(60, TimeUnit.SECONDS) // Read timeout
            .writeTimeout(60, TimeUnit.SECONDS) // Write timeout
            .build()

        fun getInstance(): Retrofit {

           return Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

        }

    }

}