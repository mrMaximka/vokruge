package ru.gb.vokruge.model.repositories.db

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.gb.vokruge.model.DBRepository
import ru.gb.vokruge.model.repositories.db.models.HistoryDB
import java.util.*

class DBRepositoryImpl(val appDatabase: AppDatabase) : DBRepository {

    override fun loadAllHistories(): Single<List<String>> {
        return appDatabase.historyDao().getAll()
            .subscribeOn(Schedulers.io())
            .map { it.map { it.text } }
    }

    override fun addStringToHistory(text: String): Single<Long> {
        return Single.create<Long> {
            it.onSuccess(appDatabase.historyDao().insert(HistoryDB(text, Date().time)))
        }.subscribeOn(Schedulers.io())
    }

    override fun clearHistory(): Single<Int> {
        return Single.create<Int> {
            it.onSuccess(appDatabase.historyDao().deleteAll())
        }.subscribeOn(Schedulers.io())
    }
}