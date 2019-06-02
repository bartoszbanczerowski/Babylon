package eu.mobilebear.babylon.presentation.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.domain.interactor.GetSocialPostsUseCase
import eu.mobilebear.babylon.domain.mapper.SocialPostMapper
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.presentation.social.navigator.SocialNavigator
import eu.mobilebear.babylon.presentation.socialdetail.adapter.PostsAdapter
import eu.mobilebear.babylon.rx.RxFactory

@Module
class SocialModule {

    @Provides
    fun provideSocialNavigator(context: Context): SocialNavigator = SocialNavigator(context)

    @Provides
    fun provideSocialPostMapper(): SocialPostMapper = SocialPostMapper()

    @Provides
    fun provideGetPostsUseCase(
        postRepository: PostRepository,
        userRepository: UserRepository,
        rxFactory: RxFactory,
        socialPostMapper: SocialPostMapper
    ): GetSocialPostsUseCase = GetSocialPostsUseCase(postRepository, userRepository, socialPostMapper, rxFactory)

    @Provides
    fun providePostsAdapter() = PostsAdapter()
}
