package com.mobdeve.group10.mco3.Repository

import com.mobdeve.group10.mco3.Server.ApiServices

// Repository class for fetching city data
class CityRepository(val api: ApiServices) {
    // Function to fetch cities based on the search query and limit
    fun getCities(q: String, limit: Int) =
        // Call the API service method to get the list of cities
        api.getCitiesList(q, limit, "0773ade61d7e97ff9b2d9a906d7670bf")
}
