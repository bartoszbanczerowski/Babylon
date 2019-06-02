package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Comment

data class CommentsValidationModel(
    val comments: List<Comment>,
    @PostsStatus val status: String
) {
    companion object {
        const val COMMENTS_DOWNLOADED = "COMMENTS_DOWNLOADED"
        const val GENERAL_ERROR = "GENERAL_ERROR"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(COMMENTS_DOWNLOADED, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
