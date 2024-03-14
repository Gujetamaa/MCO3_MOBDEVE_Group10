package com.mobdeve.group10.mco3.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobdeve.group10.mco3.DAO.CityDAO
import com.mobdeve.group10.mco3.DAO.CurrentWeatherDao
import com.mobdeve.group10.mco3.DAO.ForecastWeatherDao
import com.mobdeve.group10.mco3.model.CityResponseApi
import com.mobdeve.group10.mco3.model.CurrentResponseApi
import com.mobdeve.group10.mco3.model.ForecastResponseApi

@Database(entities = [CityResponseApi.CityResponseApiItem::class, CurrentResponseApi::class, ForecastResponseApi::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDAO
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
