package eu.mobilebear.babylon.presentation.injection.modules

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.BabylonApplication
import javax.inject.Singleton

@Module(includes = [NetworkingModule::class, ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: BabylonApplication): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun providesConnectivityManager(application: BabylonApplication): ConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun provideContext(application: BabylonApplication): Context = application

}
