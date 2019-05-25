package eu.mobilebear.babylon.presentation.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import eu.mobilebear.babylon.presentation.injection.keys.ViewModelKey
import eu.mobilebear.babylon.presentation.social.viewmodel.SocialViewModel
import eu.mobilebear.babylon.util.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SocialViewModel::class)
    abstract fun bindUserViewModel(socialViewModel: SocialViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
