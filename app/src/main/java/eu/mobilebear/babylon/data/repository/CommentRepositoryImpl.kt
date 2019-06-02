package eu.mobilebear.babylon.data.repository

import eu.mobilebear.babylon.domain.model.CommentsValidationModel
import eu.mobilebear.babylon.domain.repository.CommentRepository
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl
@Inject
constructor(
    private val socialService: SocialService,
    private val connectionChecker: ConnectionChecker
) : CommentRepository {

    override fun requestComments(): Single<CommentsValidationModel> {
        if (!connectionChecker.isConnected) return Single.error(NetworkException())
        return socialService.getComments()
            .map { response -> mapCommentResponse(response) }
            .onErrorReturn { mapCommentsError() }
    }

    private fun mapCommentResponse(response: Response<List<Comment>>): CommentsValidationModel {
        val isResponseSuccessful = response.isSuccessful && response.body() != null

        return if (isResponseSuccessful) {
            CommentsValidationModel(response.body()!!, CommentsValidationModel.COMMENTS_DOWNLOADED)
        } else {
            mapCommentsError()
        }
    }

    private fun mapCommentsError(): CommentsValidationModel = CommentsValidationModel(emptyList(), CommentsValidationModel.GENERAL_ERROR)
}
