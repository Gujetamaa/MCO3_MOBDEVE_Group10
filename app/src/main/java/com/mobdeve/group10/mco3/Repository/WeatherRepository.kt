package com.mobdeve.group10.mco3.Repository

import com.mobdeve.group10.mco3.Server.ApiServices

class WeatherRepository(val api: ApiServices) {

    fun getCurrentWeather(lat: Double,lng:Double,unit:String)=
        api.getCurrentWeather(lat,lng,unit,"f8b838ca8e25709e5702e4e83a7b74ca")

    fun getForecastWeather(lat: Double,lng:Double,unit:String)=
        api.getForecastWeather(lat,lng,unit,"f8b838ca8e25709e5702e4e83a7b74ca")
}