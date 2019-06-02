package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class Address constructor(
    @Json(name = "street") var street: String = "",
    @Json(name = "suite") var suite: String = "",
    @Json(name = "city") var city: String = "",
    @Json(name = "zipcode") var zipcode: String = "",
    @Json(name = "geo") var geoLocation: GeoLocation = GeoLocation()
)
