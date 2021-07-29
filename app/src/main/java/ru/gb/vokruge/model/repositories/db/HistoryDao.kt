package ru.gb.vokruge.model.repositories.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ru.gb.vokruge.model.repositories.db.models.HistoryDB

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY dataUsing DESC")
    fun getAll(): Single<List<HistoryDB>>

    @Query("DELETE FROM history")
    fun deleteAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(history: HistoryDB): Long

}