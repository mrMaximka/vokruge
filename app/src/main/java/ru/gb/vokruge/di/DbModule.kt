package ru.gb.vokruge.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.gb.vokruge.model.DBRepository
import ru.gb.vokruge.model.repositories.db.AppDatabase
import ru.gb.vokruge.model.repositories.db.DBRepositoryImpl
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDBRepository(appDatabase: AppDatabase): DBRepository {
        return DBRepositoryImpl(appDatabase)
    }
}