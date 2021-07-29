package ru.gb.vokruge.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.gb.vokruge.model.DBRepository
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.NetworkRepository
import ru.gb.vokruge.model.repositories.net.ApiData
import ru.gb.vokruge.model.repositories.net.INetworkStatus
import ru.gb.vokruge.model.repositories.net.NetworkRepositoryImpl
import javax.inject.Singleton

@Module(includes = [NetModule::class])
class AppModule(val applicationContext: Context) {

    @Singleton
    @Provides
    fun provideContext() = applicationContext

    @Singleton
    @Provides
    fun provideMainModel(networkRepository: NetworkRepository, dbRepository: DBRepository) =
        MainModel(networkRepository, dbRepository)

    @Singleton
    @Provides
    fun provideNetworkRepository(
        networkStatus: INetworkStatus,
        apiData: ApiData
    ): NetworkRepository =
        NetworkRepositoryImpl(networkStatus, apiData)
}
