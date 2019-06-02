package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.networking.response.responsedata.Post

data class SocialValidationModel(
    val posts: List<SocialPost>,
    @PostsStatus val status: String
) {
    companion object {
        const val POSTS_DOWNLOADED = "POSTS_DOWNLOADED"
        const val NO_POSTS = "NO_POSTS"
        const val GENERAL_ERROR = "GENERAL_LIST"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(POSTS_DOWNLOADED, NO_POSTS, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
