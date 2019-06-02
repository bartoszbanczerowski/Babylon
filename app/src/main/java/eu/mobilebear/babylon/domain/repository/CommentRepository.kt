package eu.mobilebear.babylon.domain.repository

import eu.mobilebear.babylon.domain.model.CommentsValidationModel
import io.reactivex.Single

interface CommentRepository {
    fun requestComments(): Single<CommentsValidationModel>
    fun requestComment(commentId: Int): Single<CommentsValidationModel>
}
