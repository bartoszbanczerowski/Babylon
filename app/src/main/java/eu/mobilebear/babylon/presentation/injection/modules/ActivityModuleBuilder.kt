package eu.mobilebear.babylon.presentation.injection.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import eu.mobilebear.babylon.presentation.social.view.SocialActivity
import eu.mobilebear.babylon.presentation.socialdetail.SocialDetailActivity

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector(modules = [SocialModule::class])
    abstract fun bindSocialActivity(): SocialActivity

    @ContributesAndroidInjector(modules = [SocialModule::class])
    abstract fun bindSocialDetailActivity(): SocialDetailActivity
}
