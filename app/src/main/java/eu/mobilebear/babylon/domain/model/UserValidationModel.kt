package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User

data class UserValidationModel(
    val user: User,
    @PostsStatus val status: String
) {
    companion object {
        const val USER_DOWNLOADED = "USER_DOWNLOADED"
        const val GENERAL_ERROR = "GENERAL_ERROR"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(USER_DOWNLOADED, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
