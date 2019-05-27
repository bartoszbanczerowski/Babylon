package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User

data class PostDetailValidationModel(
    val post: Post,
    val user: User,
    val comments: List<Comment>,
    @PostsStatus val status: String
) {
    companion object {
        const val SUCCESSFUL = "SUCCESSFUL"
        const val NO_POST = "NO_POST"
        const val GENERAL_ERROR = "GENERAL_LIST"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(SUCCESSFUL, NO_POST, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
