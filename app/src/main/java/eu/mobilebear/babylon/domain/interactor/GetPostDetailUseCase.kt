package eu.mobilebear.babylon.domain.interactor

import eu.mobilebear.babylon.domain.interactor.base.SingleUseCase
import eu.mobilebear.babylon.domain.model.PostDetailParamsModel
import eu.mobilebear.babylon.domain.model.PostDetailValidationModel
import eu.mobilebear.babylon.domain.model.PostslValidationAction
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.rx.RxFactory
import eu.mobilebear.babylon.util.ConnectionChecker
import io.reactivex.Single
import java.io.IOException
import javax.inject.Inject

class GetPostDetailUseCase
@Inject constructor(
    private val connectionChecker: ConnectionChecker,
    private val postRepository: PostRepository,
    rxFactory: RxFactory
) : SingleUseCase<PostslValidationAction, PostDetailParamsModel>(IO_THREAD, rxFactory) {

    override fun buildUseCaseSingle(params: PostDetailParamsModel?): Single<PostslValidationAction> {
        if (!connectionChecker.isConnected) return Single.error(IOException())

        if (params == null) {
            return Single.error(IllegalArgumentException("Post detail params should not be null!"))
        }

        val posts = postRepository.requestPosts()
        return posts.map {
            when (it.status) {
                PostDetailValidationModel.SUCCESSFUL -> PostslValidationAction.PostsDownloaded(it)
                PostDetailValidationModel.NO_POST -> PostslValidationAction.NoPosts
                else -> PostslValidationAction.GeneralError
            }
        }
    }
}
