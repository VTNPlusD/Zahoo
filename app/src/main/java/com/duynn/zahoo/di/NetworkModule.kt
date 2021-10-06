package com.duynn.zahoo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.duynn.zahoo.BuildConfig
import com.duynn.zahoo.data.error.ErrorMapper
import com.duynn.zahoo.data.model.UserDataJsonAdapter
import com.duynn.zahoo.data.repository.source.local.api.SharedPrefApi
import com.duynn.zahoo.data.repository.source.remote.api.ApiService
import com.duynn.zahoo.data.repository.source.remote.middleware.InterceptorImpl
import com.duynn.zahoo.data.repository.source.remote.response.ErrorResponseJsonAdapter
import com.duynn.zahoo.utils.constants.Constants
import com.duynn.zahoo.utils.constants.Constants.OKHTTP_TIMEOUT
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 *Created by duynn100198 on 10/04/21.
 */
private fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()
}

private fun provideErrorResponseJsonAdapter(moshi: Moshi): ErrorResponseJsonAdapter {
    return ErrorResponseJsonAdapter(moshi)
}

private fun provideErrorMapper(adapter: ErrorResponseJsonAdapter): ErrorMapper {
    return ErrorMapper(adapter)
}

private fun provideSharedPreferences(context: Context): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}

private fun provideUserLocalJsonAdapter(moshi: Moshi): UserDataJsonAdapter {
    return UserDataJsonAdapter(moshi)
}

private fun provideAuthInterceptor(sharedPrefApi: SharedPrefApi): InterceptorImpl {
    return InterceptorImpl(sharedPrefApi)
}

private fun provideRetrofit(moshi: Moshi, client: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()
}

private fun provideOkHttpClient(
    interceptorImpl: InterceptorImpl
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                }
        )
        .addInterceptor(interceptorImpl)
        .build()
}

private fun provideApiService(retrofit: Retrofit): ApiService = ApiService(retrofit)

val networkModule = module {
    single { provideMoshi() }
    factory { provideAuthInterceptor(get()) }
    factory { provideErrorResponseJsonAdapter(get()) }
    factory { provideUserLocalJsonAdapter(get()) }
    single { provideErrorMapper(get()) }
    single { provideSharedPreferences(androidApplication()) }
    single { provideRetrofit(get(), get(), get(named(Constants.KEY_BASE_URL))) }
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }
    // Other APIs
}
