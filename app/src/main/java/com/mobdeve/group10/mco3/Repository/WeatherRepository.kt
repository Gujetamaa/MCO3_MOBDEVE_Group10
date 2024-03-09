package com.mobdeve.group10.mco3.Repository

import com.mobdeve.group10.mco3.Server.ApiServices

// Repository class for fetching weather data
class WeatherRepository(val api: ApiServices) {

    // Function to fetch current weather data based on latitude, longitude, and unit
    fun getCurrentWeather(lat: Double, lng: Double, unit: String) =

        // Calls the API service method to get the current weather
        // generated API Key from openweathermap.org
        api.getCurrentWeather(lat, lng, unit, "f8b838ca8e25709e5702e4e83a7b74ca")

    // Function to fetch forecast weather data based on latitude, longitude, and unit
    fun getForecastWeather(lat: Double, lng: Double, unit: String) =
        // Call the API service method to get the forecast weather
        api.getForecastWeather(lat, lng, unit, "f8b838ca8e25709e5702e4e83a7b74ca")
}
