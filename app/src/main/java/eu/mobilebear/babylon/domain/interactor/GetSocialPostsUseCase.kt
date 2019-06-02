package eu.mobilebear.babylon.domain.interactor

import eu.mobilebear.babylon.domain.interactor.base.SingleUseCase
import eu.mobilebear.babylon.domain.mapper.SocialPostMapper
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.SocialValidationAction
import eu.mobilebear.babylon.domain.model.SocialValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetSocialPostsUseCase
@Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val socialPostMapper: SocialPostMapper,
    rxFactory: RxFactory
) : SingleUseCase<SocialValidationAction, Unit>(IO_THREAD, rxFactory) {

    override fun buildUseCaseSingle(params: Unit?): Single<SocialValidationAction> {
        return Single.zip(
            postRepository.requestPosts(),
            userRepository.requestUsers(),
            mapIntoSocialPosts()
        )
    }

    private fun mapIntoSocialPosts(): BiFunction<PostsValidationModel, UsersValidationModel, SocialValidationAction> =
        BiFunction { postsValidationModel, usersValidationModel ->
            when (postsValidationModel.status) {
                PostsValidationModel.POSTS_DOWNLOADED -> updatePostUsers(postsValidationModel.posts, usersValidationModel)
                PostsValidationModel.NO_POSTS -> SocialValidationAction.NoPosts
                else -> SocialValidationAction.GeneralError
            }

        }

    private fun updatePostUsers(posts: List<Post>, usersValidationModel: UsersValidationModel): SocialValidationAction.SocialPostsDownloaded {
        val socialPosts = mutableListOf<SocialPost>()

        when (usersValidationModel.status) {
            UsersValidationModel.USERS_DOWNLOADED -> convertPostsAndUsers(posts, usersValidationModel, socialPosts)
            else -> convertPostsOnly(posts, socialPosts)
        }

        return SocialValidationAction.SocialPostsDownloaded(SocialValidationModel(socialPosts, SocialValidationModel.POSTS_DOWNLOADED))
    }

    private fun convertPostsOnly(posts: List<Post>, socialPosts: MutableList<SocialPost>) {
        posts.forEach { socialPosts.add(socialPostMapper.transform(it)) }
    }

    private fun convertPostsAndUsers(posts: List<Post>, usersValidationModel: UsersValidationModel, socialPosts: MutableList<SocialPost>) {
        posts.forEach {
            val user = usersValidationModel.users.find { user -> it.userId == user.id }
            socialPosts.add(socialPostMapper.transform(it, user))
        }
    }
}
