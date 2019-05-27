package eu.mobilebear.babylon.presentation.injection.modules

import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.domain.interactor.GetPostsUseCase
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.presentation.social.navigator.SocialNavigator
import eu.mobilebear.babylon.presentation.social.view.adapter.PostsAdapter
import eu.mobilebear.babylon.rx.RxFactory

@Module
class SocialModule {

    @Provides
    fun provideSocialNavigator(): SocialNavigator = SocialNavigator()

    @Provides
    fun provideGetPostsUseCase(
        postRepository: PostRepository,
        rxFactory: RxFactory
    ): GetPostsUseCase = GetPostsUseCase(postRepository, rxFactory)

    @Provides
    fun providePostsAdapter() = PostsAdapter()
}
