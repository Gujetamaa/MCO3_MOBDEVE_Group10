package com.mobdeve.group10.mco3.Server

import com.mobdeve.group10.mco3.model.CityResponseApi
import com.mobdeve.group10.mco3.model.CurrentResponseApi
import com.mobdeve.group10.mco3.model.ForecastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): Call<ForecastResponseApi>


    @GET("geo/1.0/direct")
    fun getCitiesList(
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Call<CityResponseApi>
}