package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Comment

data class SocialDetailValidationModel(
    val post: SocialPost,
    @PostsStatus val status: String,
    val comments: List<Comment> = emptyList()
) {
    companion object {
        const val POST_DOWNLOADED = "POST_DOWNLOADED"
        const val NO_POSTS = "NO_POST"
        const val GENERAL_ERROR = "GENERAL_LIST"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(POST_DOWNLOADED, NO_POSTS, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
