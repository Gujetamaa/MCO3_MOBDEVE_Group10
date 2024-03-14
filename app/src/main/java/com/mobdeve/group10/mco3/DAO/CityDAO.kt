package com.mobdeve.group10.mco3.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobdeve.group10.mco3.model.CityResponseApi
import com.mobdeve.group10.mco3.model.CurrentResponseApi
import com.mobdeve.group10.mco3.model.ForecastResponseApi

@Dao
interface CityDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityResponseApi.CityResponseApiItem>)

    @Query("SELECT * FROM CityResponseApiItem WHERE name LIKE '%' || :query || '%'")
    fun searchCities(query: String): List<CityResponseApi.CityResponseApiItem>
}

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentResponseApi)

    @Query("SELECT * FROM CurrentResponseApi LIMIT 1")
    fun getCurrentWeather(): CurrentResponseApi?
}

@Dao
interface ForecastWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastWeather(forecast: ForecastResponseApi)

    @Query("SELECT * FROM ForecastResponseApi LIMIT 1")
    fun getForecastWeather(): ForecastResponseApi?
}
