package eu.mobilebear.babylon.networking.response.responsedata

import com.squareup.moshi.Json

data class Comment constructor(
    @Json(name = "postId") var postId: Int,
    @Json(name = "id") var id: Int,
    @Json(name = "name") var title: String,
    @Json(name = "body") var body: String,
    @Json(name = "email") var email: String
)
