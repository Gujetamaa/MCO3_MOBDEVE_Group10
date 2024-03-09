package com.mobdeve.group10.mco3.Server

import com.mobdeve.group10.mco3.model.CityResponseApi
import com.mobdeve.group10.mco3.model.CurrentResponseApi
import com.mobdeve.group10.mco3.model.ForecastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Interface defining API endpoints for weather and city information
interface ApiServices {

    // Endpoint to get current weather data
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,    // Latitude of the location
        @Query("lon") lon: Double,    // Longitude of the location
        @Query("units") units: String,    // Units of measurement (e.g., metric or imperial)
        @Query("appid") apiKey: String,    // API key for authentication
    ): Call<CurrentResponseApi>

    // Endpoint to get forecast weather data
    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat: Double,    // Latitude of the location
        @Query("lon") lon: Double,    // Longitude of the location
        @Query("units") units: String,    // Units of measurement (e.g., metric or imperial)
        @Query("appid") apiKey: String,    // API key for authentication
    ): Call<ForecastResponseApi>

    // Endpoint to search for cities
    @GET("geo/1.0/direct")
    fun getCitiesList(
        @Query("q") q: String,    // Query string for city search
        @Query("limit") limit: Int,    // Maximum number of results to return
        @Query("appid") apiKey: String    // API key for authentication
    ): Call<CityResponseApi>
}
