package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User

data class UsersValidationModel(
    val users: List<User>,
    @PostsStatus val status: String
) {
    companion object {
        const val USERS_DOWNLOADED = "USERS_DOWNLOADED"
        const val NO_USERS = "NO_USERS"
        const val GENERAL_ERROR = "GENERAL_ERROR"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(USERS_DOWNLOADED, NO_USERS, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
