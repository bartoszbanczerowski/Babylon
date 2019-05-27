package eu.mobilebear.babylon.domain.repository

import eu.mobilebear.babylon.domain.model.PostDetailValidationModel
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import io.reactivex.Single

interface PostRepository {
    fun requestPosts(): Single<PostsValidationModel>
    fun requestPost(postId: Int): Single<PostDetailValidationModel>
}
