package com.mobdeve.group10.mco3.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.matteobattilana.weather.PrecipType
import com.mobdeve.group10.mco3.Adapter.ForecastAdapter
import com.mobdeve.group10.mco3.R
import com.mobdeve.group10.mco3.SettingsActivity
import com.mobdeve.group10.mco3.ViewModel.WeatherViewModel
import com.mobdeve.group10.mco3.databinding.ActivityMainBinding
import com.mobdeve.group10.mco3.model.CurrentResponseApi
import com.mobdeve.group10.mco3.model.ForecastResponseApi
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }
    private var isSwitchChecked = true


    private var lat: Double = 0.0
    private var lon: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the settings button
        val settingsButton = findViewById<ImageView>(R.id.settingsIv)

        // Set click listener to navigate to the SettingsActivity
        settingsButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.apply {
                systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }



        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            // Retrieve data from the intent or use default values
             lat = intent.getDoubleExtra("lat", 0.0)
             lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")

            // manual setting of Manila when the app runs
            if (lat == 0.0) {
                lat = 14.60
                lon = 121.02
                name = "Manila"
            }

            // Handle button click to open CityListActivity
            addCityIv.setOnClickListener {
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

            // Load current weather data
            cityTv.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi> {
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            // Update UI with current weather data
                            statusTv.text = it.weather?.get(0)?.main ?: "-"
                            windTv.text =
                                it.wind?.speed?.let { speed -> "${Math.round(speed)}Km" } ?: ""
                            humidityTv.text = it.main?.humidity?.toString() + "%"
                            currentTempTv.text = if (isSwitchChecked) {
                                "${Math.round(it.main?.temp ?: 0.0)}°"
                            } else {
                                convertCelsiusToFahrenheit("${it.main?.temp ?: 0.0}°")
                            }
                            maxTempTv.text = if (isSwitchChecked) {
                                "${Math.round(it.main?.tempMax ?: 0.0)}°"
                            } else {
                                convertCelsiusToFahrenheit("${it.main?.tempMax ?: 0.0}°")
                            }
                            minTempTv.text = if (isSwitchChecked) {
                                "${Math.round(it.main?.tempMin ?: 0.0)}°"
                            } else {
                                convertCelsiusToFahrenheit("${it.main?.tempMin ?: 0.0}°")
                            }

                            // Set background image based on day/night and weather condition
                            val drawable = if (isNightNow()) R.drawable.night_bg
                            else {
                                setDynamicallyWallpaper(it.weather?.get(0)?.icon ?: "-")
                            }
                            backgroundIv.setImageResource(drawable)
                            // Set weather effects for rain or snow
                            setEffectRainSnow(it.weather?.get(0)?.icon ?: "-")
                        }
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    // Handle failure to load current weather data
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            // Set up blur view for the background
            var radius = 10f
            val decorView = window.decorView
            val rootView = (decorView.findViewById(android.R.id.content) as ViewGroup?)
            val windowBackground = decorView.background

            rootView?.let {
                blurView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blurView.clipToOutline = true
            }

            // Load forecast weather data
            weatherViewModel.loadForecastWeather(lat, lon, "metric")
                .enqueue(object : retrofit2.Callback<ForecastResponseApi> {
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            blurView.visibility = View.VISIBLE

                            data?.let {
                                // Update forecast RecyclerView with data
                                forecastAdapter.differ.submitList(it.list)
                                forecastView.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                        // Handle failure to load forecast weather data
                    }
                })
        }
    }

    // Check if it is night based on the current time
    private fun isNightNow(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    // Set the dynamically wallpaper based on the weather icon
    private fun setDynamicallyWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }

            "13" -> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }

            else -> 0
        }
    }

    // Set weather effects for rain or snow
    private fun setEffectRainSnow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "02", "03", "04" -> {
                initWeatherView(PrecipType.CLEAR)
            }

            "09", "10", "11" -> {
                initWeatherView(PrecipType.RAIN)
            }

            "13" -> {
                initWeatherView(PrecipType.SNOW)
            }

            "50" -> {
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }

    // Initialize the weather view based on the precipitation type
    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate = 100.0f
        }
    }

    // Function to convert Celsius temperature to Fahrenheit
    private fun convertCelsiusToFahrenheit(celsius: String): String {
        val celsiusTemp = celsius.substringBefore("°").toFloat()
        val fahrenheitTemp = (celsiusTemp * 9 / 5) + 32
        return "${Math.round(fahrenheitTemp)}°"
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val SETTINGS_REQUEST_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
            // Reload current weather data with updated settings
            weatherViewModel.loadCurrentWeather(lat, lon, "metric")
        }
    }

}
