package eu.mobilebear.babylon.presentation.injection.modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import eu.mobilebear.babylon.BuildConfig
import eu.mobilebear.babylon.networking.SocialService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkingModule {

    companion object {
        private const val REQUEST_HEADER_USER_AGENT_KEY = "User-Agent"
        private const val REQUEST_HEADER_USER_AGENT_VALUE = "Android"
        private const val HOST = "http://jsonplaceholder.typicode.com/"
        private const val CONNECT_TIMEOUT = 60L
        private const val READ_TIMEOUT = 60L
        private const val WRITE_TIMEOUT = 60L
    }

    @Provides
    @Singleton
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) Level.BASIC else Level.NONE
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                    .header(
                        REQUEST_HEADER_USER_AGENT_KEY,
                        REQUEST_HEADER_USER_AGENT_VALUE
                    )
                    .build()
            chain.proceed(request)
        }


    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            headerInterceptor: Interceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addNetworkInterceptor(loggingInterceptor)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .build()

    @Provides
    @Singleton
    fun provideMoshiBuilder(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(HOST)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    @Provides
    @Singleton
    fun provideSocialService(retrofit: Retrofit): SocialService = retrofit.create(SocialService::class.java)
}
