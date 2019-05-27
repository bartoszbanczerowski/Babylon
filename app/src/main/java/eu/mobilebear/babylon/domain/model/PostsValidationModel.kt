package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Post

data class PostsValidationModel(
    val posts: List<Post>,
    @PostsStatus val status: String
) {
    companion object {
        const val POSTS_DOWNLOADED = "POSTS_DOWNLOADED"
        const val EMPTY_LIST = "EMPTY_LIST"
        const val GENERAL_ERROR = "GENERAL_LIST"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(POSTS_DOWNLOADED, EMPTY_LIST, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
