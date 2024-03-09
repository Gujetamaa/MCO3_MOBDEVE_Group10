package com.mobdeve.group10.mco3.Server

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Singleton class to provide Retrofit client for API requests
class ApiClient {
    private lateinit var retrofit: Retrofit

    // OkHttpClient for configuring connection timeouts
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
        .readTimeout(60, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
        .build()

    // Function to get the Retrofit client instance
    fun getClient(): Retrofit {
        // Initialize Retrofit instance with base URL, OkHttpClient, and GsonConverterFactory
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org") // Base URL for API requests
            .client(client) // OkHttpClient for handling network operations
            .addConverterFactory(GsonConverterFactory.create()) // Converter for JSON to Kotlin object mapping
            .build()
        return retrofit
    }
}
