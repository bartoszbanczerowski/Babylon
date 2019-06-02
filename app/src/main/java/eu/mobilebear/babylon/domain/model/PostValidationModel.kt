package eu.mobilebear.babylon.domain.model

import androidx.annotation.StringDef
import eu.mobilebear.babylon.networking.response.responsedata.Post

data class PostValidationModel(
    val post: Post,
    @PostsStatus val status: String
) {
    companion object {
        const val POST_DOWNLOADED = "POST_DOWNLOADED"
        const val GENERAL_ERROR = "GENERAL_LIST"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(POST_DOWNLOADED, GENERAL_ERROR)
        annotation class PostsStatus
    }
}
