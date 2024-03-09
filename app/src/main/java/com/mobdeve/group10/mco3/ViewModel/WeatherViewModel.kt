package com.mobdeve.group10.mco3.ViewModel

import androidx.lifecycle.ViewModel
import com.mobdeve.group10.mco3.Repository.WeatherRepository
import com.mobdeve.group10.mco3.Server.ApiClient
import com.mobdeve.group10.mco3.Server.ApiServices

// ViewModel class for managing weather-related data
class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    // Secondary constructor to create a default WeatherRepository instance
    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    // Function to load current weather data based on latitude, longitude, and unit
    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        repository.getCurrentWeather(lat, lng, unit)

    // Function to load forecast weather data based on latitude, longitude, and unit
    fun loadForecastWeather(lat: Double, lng: Double, unit: String) =
        repository.getForecastWeather(lat, lng, unit)
}
