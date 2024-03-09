package com.mobdeve.group10.mco3.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group10.mco3.Activity.MainActivity
import com.mobdeve.group10.mco3.databinding.CityViewholderBinding
import com.mobdeve.group10.mco3.model.CityResponseApi

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    // lateinit used for late initialization of binding
    private lateinit var binding: CityViewholderBinding

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CityViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = CityViewholderBinding.bind(holder.itemView)
        // Set city name in TextView
        binding.cityTxt.text = differ.currentList[position].name
        // Open MainActivity when item clicked, passing latitude, longitude, and name
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            intent.putExtra("lat", differ.currentList[position].lat)
            intent.putExtra("lon", differ.currentList[position].lon)
            intent.putExtra("name", differ.currentList[position].name)
            binding.root.context.startActivity(intent)
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    // Return number of items in list
    override fun getItemCount() = differ.currentList.size

    // DiffUtil callback for calculating differences between lists
    private val differCallback =
        object : DiffUtil.ItemCallback<CityResponseApi.CityResponseApiItem>() {
            override fun areItemsTheSame(
                oldItem: CityResponseApi.CityResponseApiItem,
                newItem: CityResponseApi.CityResponseApiItem
            ): Boolean {
                // Check if items are the same
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CityResponseApi.CityResponseApiItem,
                newItem: CityResponseApi.CityResponseApiItem
            ): Boolean {
                // Check if contents are the same
                return oldItem == newItem
            }
        }

    // AsyncListDiffer to compute differences between current and new list
    val differ = AsyncListDiffer(this, differCallback)
}
