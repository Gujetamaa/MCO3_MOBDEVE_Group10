package com.mobdeve.group10.mco3.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.group10.mco3.databinding.ForecastViewholderBinding
import com.mobdeve.group10.mco3.model.ForecastResponseApi
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    // lateinit used for late initialization of binding
    private lateinit var binding: ForecastViewholderBinding

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ForecastAdapter.ViewHolder, position: Int) {
        val binding = ForecastViewholderBinding.bind(holder.itemView)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date

        // Set day of the week
        val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        binding.nameDayTxt.text = dayOfWeekName

        // Set hour and AM/PM
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPm = if (hour < 12) "am" else "pm"
        val hour12 = calendar.get(Calendar.HOUR)
        binding.hourTxt.text = hour12.toString() + amPm

        // Set temperature
        binding.tempTxt.text = differ.currentList[position].main?.temp?.let { Math.round(it) }.toString() + "°"

        // Set weather icon
        val icon = when (differ.currentList[position].weather?.get(0)?.icon.toString()) {
            "01d", "0n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }

        // Get drawable resource ID based on icon name
        val drawableResourceId: Int = binding.root.resources.getIdentifier(
            icon,
            "drawable", binding.root.context.packageName
        )

        // Load weather icon using Glide
        Glide.with(binding.root.context)
            .load(drawableResourceId)
            .into(binding.pic)
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    // Return number of items in list
    override fun getItemCount() = differ.currentList.size

    // DiffUtil callback for calculating differences between lists
    private val differCallback = object : DiffUtil.ItemCallback<ForecastResponseApi.data>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            // Check if items are the same
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            // Check if contents are the same
            return oldItem == newItem
        }
    }

    // AsyncListDiffer to compute differences between current and new list
    val differ = AsyncListDiffer(this, differCallback)
}
