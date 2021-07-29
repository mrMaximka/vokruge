package ru.gb.vokruge.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.models.QueryCompanies
import timber.log.Timber
import javax.inject.Inject


class SearchViewModel : ViewModel() {

    @Inject
    lateinit var model: MainModel

    var currentString: String = ""
    val suitableStrings = MutableLiveData<List<String>>().apply { value = emptyList() }
    var needShowSearch = MutableLiveData<Boolean>().apply { value = false }
    var needShowHistoryList = MutableLiveData<Boolean>().apply { value = false }

    init {
        App.appComponent.inject(this)
    }

    fun onClickShow(text: String) {
        currentString = text
        model.addStringToHistory(text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ count -> Timber.d("Saved") }, { t -> Timber.e(t) })
        model.queryCompanies.value =
            QueryCompanies(type = QueryCompanies.QueryType.SEARCH_STRING, query = text)
        model.currentCompanies.value = null
        model.panelState.value = MainModel.States.LIST_VISIBLE
        needShowSearch.value = true
    }

    fun refreshSuitableStrings(text: String) {
        currentString = text
        model.getHistoryStrings(text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ datas ->
                suitableStrings.value = datas
                needShowHistoryList.value = datas.size > 0
            }, { t: Throwable? ->
                Timber.d(t)
                suitableStrings.value = emptyList()
                needShowHistoryList.value = false
            })
    }

    fun clearHistoryClick() {
        model.clearHistory()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ count ->
                suitableStrings.value = emptyList()
                needShowHistoryList.value = false
            })
    }
}
