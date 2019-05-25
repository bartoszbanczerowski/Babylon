package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class Company constructor(
    @Json(name = "name") var name: String,
    @Json(name = "catchPhrase") var catchPhrase: String,
    @Json(name = "bs") var businessSection: String
)
