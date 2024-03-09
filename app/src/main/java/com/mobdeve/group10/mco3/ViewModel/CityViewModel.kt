package com.mobdeve.group10.mco3.ViewModel

import androidx.lifecycle.ViewModel
import com.mobdeve.group10.mco3.Repository.CityRepository
import com.mobdeve.group10.mco3.Server.ApiClient
import com.mobdeve.group10.mco3.Server.ApiServices

class CityViewModel(val repository: CityRepository) : ViewModel() {
    constructor() : this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCity(q: String, limit: Int) =
        repository.getCities(q, limit)
}