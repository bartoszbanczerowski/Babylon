package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class Post constructor(
    @Json(name = "userId") var userId: Int,
    @Json(name = "id") var id: Int,
    @Json(name = "title") var title: String,
    @Json(name = "body") var body: String
)
