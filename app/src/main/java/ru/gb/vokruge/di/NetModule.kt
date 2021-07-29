package ru.gb.vokruge.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.vokruge.model.repositories.net.ApiData
import ru.gb.vokruge.model.repositories.net.CacheInterceptor
import ru.gb.vokruge.model.repositories.net.INetworkStatus
import ru.gb.vokruge.ui.utils.NetworkStatus
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetModule {

    @Named("baseUrl")
    @Provides
    fun baseUrl(): String {
        return "http://51.158.167.236/"
    }

    @Singleton
    @Provides
    fun apiService(@Named("clientLogging") okHttpClient: OkHttpClient, gson: Gson, @Named("baseUrl") baseUrl: String): ApiData {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiData::class.java )
    }


    @Singleton
    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Named("clientLogging")
    @Singleton
    @Provides
    fun okHttpClientLogging(
        loggingInterceptor: HttpLoggingInterceptor,
        context: Context
    ): OkHttpClient {
        val httpCacheDirectory = File(context.getCacheDir(), "responses")
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(httpCacheDirectory, cacheSize)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(CacheInterceptor())
            .cache(cache)
            .build()
    }

    @Named("client")
    @Singleton
    @Provides
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun gson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }

    @Singleton
    @Provides
    fun networkStatus(): INetworkStatus {
        return NetworkStatus()
    }

}