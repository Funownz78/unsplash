package com.example.imageshering.di

import com.example.imageshering.App
import com.example.imageshering.data.api.UnsplashApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain -> return@addInterceptor addApiAuthorizationToHeader(chain) }
            .build()

        return Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

    private fun addApiAuthorizationToHeader(chain: Interceptor.Chain): Response {
        val authComponent = App.daggerComponent.getAppAuthComponent()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${authComponent.accessToken}")
            .build()
        return chain.proceed(request)
    }
}