package eu.mobilebear.babylon.presentation.injection.modules

import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.presentation.social.navigator.SocialNavigator

@Module
class SocialModule{

    @Provides
    fun provideSocialNavigator(): SocialNavigator = SocialNavigator()
}
