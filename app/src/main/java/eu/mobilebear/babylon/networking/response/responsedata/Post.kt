package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class Post constructor(
    @Json(name = "userId") var userId: Int = -1,
    @Json(name = "id") var id: Int = -1,
    @Json(name = "title") var title: String = "",
    @Json(name = "body") var body: String = ""
)
