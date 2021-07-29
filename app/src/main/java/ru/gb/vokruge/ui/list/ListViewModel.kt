package ru.gb.vokruge.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.model.models.CompanyInfo
import ru.gb.vokruge.model.models.QueryCompanies
import timber.log.Timber
import javax.inject.Inject


class ListViewModel : ViewModel() {

    @Inject
    lateinit var model: MainModel

    val needShowProgress = MutableLiveData<Boolean>().apply { value = false }

    init {
        App.appComponent.inject(this)
    }

    fun getCurrentCompanies() = model.currentCompanies

    fun createList() {
        getRequest()?.apply {
            clearList()
            needShowProgress.value = true

            observeOn(AndroidSchedulers.mainThread())
                .subscribe({ datas ->
                    model.currentCompanies.value = datas
                    needShowProgress.value = false
                }, { throwable ->
                    Timber.d(throwable)
                    needShowProgress.value = false
                })
        }
    }

    private fun clearList() {
        model.currentCompanies.value = ArrayList<CompanyInfo>()
    }

    private fun getRequest(): Single<List<CompanyInfo>>? {
        model.queryCompanies.value?.let {
            when (it.type) {
                QueryCompanies.QueryType.SEARCH_STRING ->
                    return model.loadCompanies(it.query)
                QueryCompanies.QueryType.SUBCATEGORY ->
                    return model.loadCompanies(it.idCategory)
            }
        }
        return null
    }

    fun setCurrentCompany(company: CompanyInfo) {
        model.currentCompany.value = company
        panelState().value = MainModel.States.SHORT_DETAIL
    }

    fun panelState() = model.panelState
}


