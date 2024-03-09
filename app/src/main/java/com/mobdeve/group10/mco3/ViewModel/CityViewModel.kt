package com.mobdeve.group10.mco3.ViewModel

import androidx.lifecycle.ViewModel
import com.mobdeve.group10.mco3.Repository.CityRepository
import com.mobdeve.group10.mco3.Server.ApiClient
import com.mobdeve.group10.mco3.Server.ApiServices

// ViewModel class for managing city-related data
class CityViewModel(val repository: CityRepository) : ViewModel() {

    // Secondary constructor to create a default CityRepository instance
    constructor() : this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    // Function to load city data based on the query string and limit
    fun loadCity(q: String, limit: Int) =
        repository.getCities(q, limit)
}
