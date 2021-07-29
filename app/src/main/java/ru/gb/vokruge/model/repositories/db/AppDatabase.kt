package ru.gb.vokruge.model.repositories.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gb.vokruge.model.repositories.db.models.HistoryDB

@Database(entities = [HistoryDB::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao() : HistoryDao
}