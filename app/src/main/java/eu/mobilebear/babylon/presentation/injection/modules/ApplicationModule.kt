package eu.mobilebear.babylon.presentation.injection.modules

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.BabylonApplication
import eu.mobilebear.babylon.util.AndroidObjectsFactory
import eu.mobilebear.babylon.util.ConnectionChecker
import javax.inject.Singleton

@Module(includes = [RxModule::class, NetworkingModule::class, ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: BabylonApplication): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun providesConnectivityManager(application: BabylonApplication, androidObjectsFactory: AndroidObjectsFactory): ConnectionChecker = ConnectionChecker(application, androidObjectsFactory)

    @Provides
    @Singleton
    fun providesAndroidObjectFactory() = AndroidObjectsFactory()

    @Singleton
    @Provides
    fun provideContext(application: BabylonApplication): Context = application

}
