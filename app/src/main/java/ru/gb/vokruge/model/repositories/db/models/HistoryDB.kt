package ru.gb.vokruge.model.repositories.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryDB(

    @PrimaryKey
    var text: String = "",

    var dataUsing: Long = 0L

)
