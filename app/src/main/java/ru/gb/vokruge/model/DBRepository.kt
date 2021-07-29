package ru.gb.vokruge.model

import io.reactivex.Single

interface DBRepository {

    fun loadAllHistories(): Single<List<String>>

    fun addStringToHistory(text: String): Single<Long>

    fun clearHistory(): Single<Int>

}