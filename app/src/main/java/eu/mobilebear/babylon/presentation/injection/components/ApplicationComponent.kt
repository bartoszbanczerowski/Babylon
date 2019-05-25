package eu.mobilebear.babylon.presentation.injection.components

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import eu.mobilebear.babylon.BabylonApplication
import eu.mobilebear.babylon.presentation.injection.modules.ActivityModuleBuilder
import eu.mobilebear.babylon.presentation.injection.modules.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModuleBuilder::class,
        ApplicationModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BabylonApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(application: BabylonApplication): Builder

        fun build(): ApplicationComponent
    }
}
