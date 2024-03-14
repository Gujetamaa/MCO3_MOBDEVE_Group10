package com.mobdeve.group10.mco3.Repository

import com.mobdeve.group10.mco3.Server.ApiServices

// Repository class for fetching city data
class CityRepository(val api: ApiServices) {


    // Function to fetch cities based on the search query and limit
    fun getCities(q: String, limit: Int) =
        // Call the API service method to get the list of cities
        api.getCitiesList(q, limit, "f8b838ca8e25709e5702e4e83a7b74ca")
}
