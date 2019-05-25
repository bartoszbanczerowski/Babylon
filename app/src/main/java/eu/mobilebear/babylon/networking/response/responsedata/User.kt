package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class User constructor(
    @Json(name = "id") var id: Int,
    @Json(name = "username") var username: String,
    @Json(name = "email") var email: String,
    @Json(name = "address") var address: Address,
    @Json(name = "phone") var phone: String,
    @Json(name = "website") var website: String,
    @Json(name = "company") var company: Company
)
