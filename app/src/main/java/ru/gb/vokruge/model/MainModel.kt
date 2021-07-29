package ru.gb.vokruge.model

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import ru.gb.vokruge.model.models.CompanyCategory
import ru.gb.vokruge.model.models.CompanyInfo
import ru.gb.vokruge.model.models.QueryCompanies
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set

class MainModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DBRepository
) {

    private var historyStrings = ArrayList<String>()
    private var categories: List<CompanyCategory>? = null
    private val subCategories = HashMap<Int, List<CompanyCategory>>()

    var allCategories: List<CompanyCategory> = emptyList()

    var currentCompany = MutableLiveData<CompanyInfo>()
    var currentCompanies = MutableLiveData<List<CompanyInfo>>()
    var queryCompanies = MutableLiveData<QueryCompanies>()
    var mapVisible = false
    var panelState = MutableLiveData<States>().apply { value = States.SHORT_CATEGORY }


    fun loadAllCategories() {
        networkRepository.loadAllCategories()
            .doOnSuccess { allCategories = it }
            .subscribe()
    }

    fun loadCompanies(query: String) =
        networkRepository.loadCompanies(query).doOnSuccess { companies ->
            companies.forEach {
                it.categoryString = getCategoryString(it.categories)
            }
        }

    private fun getCategoryString(categoryIds: List<Int>): String {
        return allCategories.filter { categoryIds.contains(it.id) }
            .joinToString { it.nameCategory }
    }

    fun loadCompanies(idSubcategory: Int) =
        networkRepository.loadCompanies(idSubcategory).doOnSuccess { companies ->
            companies.forEach {
                it.categoryString = getCategoryString(it.categories)
            }
        }

    fun loadCompany(id: Int) = networkRepository.loadCompany(id).doOnSuccess { company ->
        company.categoryString = getCategoryString(company.categories)
    }

    fun loadSubcategories(idCategory: Int): List<CompanyCategory> {
        return if (subCategories[idCategory] == null) {
            allCategories.filter { category -> category.parent == idCategory }
                .sortedBy { category -> category.nameCategory }
                .apply { subCategories[idCategory] = this }
        } else {
            subCategories[idCategory]!!
        }
    }

    fun loadCategories(): List<CompanyCategory> {
        return if (categories == null) {
            allCategories.filter { category -> category.parent == null }
                .sortedBy { category -> category.nameCategory }
                .apply { categories = this }
        } else {
            categories!!
        }
    }

    fun getHistoryStrings(text: String): Single<List<String>> {
        if (historyStrings.size == 0) {
            return dbRepository.loadAllHistories()
                .doOnSuccess { historyStrings.addAll(it) }
                .map { it.filter { it.contains(text, true) } }
        } else {
            return Single.just(historyStrings.filter { it.contains(text, true) })
        }
    }

    fun addStringToHistory(text: String): Single<Long> {
        historyStrings.remove(text)
        historyStrings.add(0, text)
        return dbRepository.addStringToHistory(text)
    }

    fun clearHistory(): Single<Int> {
        return dbRepository.clearHistory()
            .doOnSuccess { historyStrings.clear() }
    }

    fun onBackSearchClick(): Boolean {
        if (mapVisible) {
            if (panelState.value == States.SHORT_DETAIL) {
                panelState.value = States.LIST_VISIBLE
                return true
            } else if (panelState.value == States.LIST_VISIBLE) {
                panelState.value = States.SHORT_CATEGORY
                return true
            }
        }
        return false
    }

    enum class States {
        SHORT_CATEGORY,
        SHORT_DETAIL,
        LIST_VISIBLE,
        LIST_HIDEN
    }

}