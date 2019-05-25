package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class GeoLocation constructor(
    @Json(name = "lat") var lat: String,
    @Json(name = "lng") var lng: String
)
