object Versions {
    const val kotlinVersion = "1.3.21"

    const val androidxAppCompatVersion = "1.0.2"
    const val androidxCoreVersion = "1.0.2"
    const val constraintLayoutVersion = "1.1.3"
    const val androidMaterialVersion = "1.0.0"
    const val androidxMotionLayoutVersion = "2.0.0-beta1"
    const val daggerVersion = "2.22.1"
    const val rxKotlinVersion = "2.3.0"
    const val rxAndroidVersion = "2.1.1"
    const val retrofitVersion = "2.5.0"
    const val okHttpVersion = "3.14.2"
    const val moshiVersion = "1.8.0"
    const val picassoVersion = "2.71828"
    const val timberVersion = "4.7.1"
    const val crashlyticsVersion = "2.10.0"
    const val mockitoVersion = "2.27.0"
    const val junitVersion = "4.12"
    const val androidxTestRunnerVersion = "1.1.1"
    const val androidxEspressoVersion = "3.1.1"
    const val lottieVersion = "3.0.3"
}

object Libs {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompatVersion}"
    val androidxCore = "androidx.core:core-ktx:${Versions.androidxCoreVersion}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    val androidMaterial = "com.google.android.material:material:${Versions.androidMaterialVersion}"
    val androidxMotionLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidxMotionLayoutVersion}"

    val dagger = "com.google.dagger:dagger:${Versions.daggerVersion}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.daggerVersion}"
    val daggerAndroid = "com.google.dagger:dagger-android:${Versions.daggerVersion}"
    val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.daggerVersion}" // if you use the support libraries
    val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.daggerVersion}"

    val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlinVersion}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroidVersion}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}"
    val retrofitRxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"
    val okHttp3 = "com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}"
    val okHttp3LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpVersion}"
    val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshiVersion}"
    val moshiKotlinCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiVersion}"
    val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.moshiVersion}"

    val picasso = "com.squareup.picasso:picasso:${Versions.picassoVersion}"
    val timber = "com.jakewharton.timber:timber:${Versions.timberVersion}"
    val lottie = "com.airbnb.android:lottie:${Versions.lottieVersion}"

    val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlyticsVersion}@aar"

    val mockito = "org.mockito:mockito-core:${Versions.mockitoVersion}"
    val junit = "junit:junit:${Versions.junitVersion}"
    val androidxTestRunner = "androidx.test:runner:${Versions.androidxTestRunnerVersion}"
    val androidxEspresso = "androidx.test.espresso:espresso-core:${Versions.androidxEspressoVersion}"
}
