package eu.mobilebear.babylon.presentation.injection.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.data.repository.CommentRepositoryImpl
import eu.mobilebear.babylon.data.repository.PostRepositoryImpl
import eu.mobilebear.babylon.data.repository.UserRepositoryImpl
import eu.mobilebear.babylon.domain.repository.CommentRepository
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.rx.RxFactory
import javax.inject.Singleton

@Module
abstract class RxModule {

    @Module
    companion object {

        @Provides
        @Singleton
        fun providesRxFactory() = RxFactory()
    }

    @Binds
    @Singleton
    abstract fun providesPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun providesUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun providesCommentsRepository(commentRepositoryImpl: CommentRepositoryImpl): CommentRepository
}
