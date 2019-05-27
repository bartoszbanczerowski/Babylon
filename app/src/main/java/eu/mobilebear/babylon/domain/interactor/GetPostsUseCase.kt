package eu.mobilebear.babylon.domain.interactor

import eu.mobilebear.babylon.domain.interactor.base.SingleUseCase
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.model.PostslValidationAction
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Single
import javax.inject.Inject

class GetPostsUseCase
@Inject constructor(
    private val postRepository: PostRepository,
    rxFactory: RxFactory
) : SingleUseCase<PostslValidationAction, Unit>(IO_THREAD, rxFactory) {

    override fun buildUseCaseSingle(params: Unit?): Single<PostslValidationAction> {
        return postRepository.requestPosts().map {
            when (it.status) {
                PostsValidationModel.POSTS_DOWNLOADED -> PostslValidationAction.PostsDownloaded(it)
                PostsValidationModel.EMPTY_LIST -> PostslValidationAction.NoPosts
                else -> PostslValidationAction.GeneralError
            }
        }
    }
}
