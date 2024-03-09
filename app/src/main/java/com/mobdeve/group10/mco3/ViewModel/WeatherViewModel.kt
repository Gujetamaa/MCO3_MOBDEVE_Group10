package com.mobdeve.group10.mco3.ViewModel

import androidx.lifecycle.ViewModel
import com.mobdeve.group10.mco3.Repository.WeatherRepository
import com.mobdeve.group10.mco3.Server.ApiClient
import com.mobdeve.group10.mco3.Server.ApiServices

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lng: Double, unit: String) =
        repository.getCurrentWeather(lat, lng, unit)

    fun loadForecastWeather(lat: Double, lng: Double, unit: String) =
        repository.getForecastWeather(lat, lng, unit)
}