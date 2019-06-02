package eu.mobilebear.babylon.domain.interactor

import eu.mobilebear.babylon.domain.interactor.base.SingleUseCase
import eu.mobilebear.babylon.domain.mapper.SocialPostMapper
import eu.mobilebear.babylon.domain.model.CommentsValidationModel
import eu.mobilebear.babylon.domain.model.PostDetailParamsModel
import eu.mobilebear.babylon.domain.model.PostValidationModel
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.model.SocialDetailValidationAction
import eu.mobilebear.babylon.domain.model.SocialDetailValidationModel
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.UserValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.domain.repository.CommentRepository
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

class GetSocialPostDetailUseCase
@Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val socialPostMapper: SocialPostMapper,
    rxFactory: RxFactory
) : SingleUseCase<SocialDetailValidationAction, PostDetailParamsModel>(IO_THREAD, rxFactory) {

    override fun buildUseCaseSingle(params: PostDetailParamsModel?): Single<SocialDetailValidationAction> {
        if (params == null) {
            return Single.error(IllegalArgumentException("Post detail params should not be null!"))
        }

        return Single.zip(
            postRepository.requestPost(params.postId),
            userRepository.requestUser(params.userId),
            commentRepository.requestComments(),
            mapIntoSocialDetailPost()
        )
    }

    private fun mapIntoSocialDetailPost(): Function3<PostValidationModel, UserValidationModel, CommentsValidationModel, SocialDetailValidationAction> =
        Function3 { postsValidationModel, usersValidationModel, commentsValidationModel ->
            when (postsValidationModel.status) {
                PostsValidationModel.POSTS_DOWNLOADED -> updatePostUsers(postsValidationModel.post, usersValidationModel, commentsValidationModel)
                PostsValidationModel.NO_POSTS -> SocialDetailValidationAction.NoPost
                else -> SocialDetailValidationAction.GeneralError
            }

        }

    private fun updatePostUsers(
        post: Post,
        usersValidationModel: UserValidationModel,
        commentsValidationModel: CommentsValidationModel
    ): SocialDetailValidationAction {

        val socialPost = when (usersValidationModel.status) {
            UsersValidationModel.USERS_DOWNLOADED -> convertPostAndUser(post, usersValidationModel)
            else -> convertPostsOnly(post)
        }

        return if (commentsValidationModel.status == CommentsValidationModel.COMMENTS_DOWNLOADED) {
            SocialDetailValidationAction.SocialPostDownloaded(
                SocialDetailValidationModel(socialPost, SocialDetailValidationModel.POST_DOWNLOADED, commentsValidationModel.comments)
            )
        } else {
            SocialDetailValidationAction.SocialPostDownloaded(SocialDetailValidationModel(socialPost, SocialDetailValidationModel.POST_DOWNLOADED))
        }
    }

    private fun convertPostsOnly(post: Post) = socialPostMapper.transform(post)

    private fun convertPostAndUser(post: Post, usersValidationModel: UserValidationModel): SocialPost = socialPostMapper.transform(post, usersValidationModel.user)
}
